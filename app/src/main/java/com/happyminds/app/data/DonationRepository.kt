package com.happyminds.app.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

/** The address of your deployed Cloudflare Worker (printed by `npx wrangler deploy`). */
const val WORKER_URL = "https://happyminds-payfast.connecthub-za.workers.dev"

/**
 * Payments go through PayFast:
 *   app -> Worker /create (with the user's Firebase token) -> signed PayFast URL -> Custom Tab
 *   PayFast -> Worker /itn -> Worker marks the donation COMPLETE in Firestore
 * The merchant key and passphrase live only in the Worker, never in the app.
 */
class DonationRepository {

    private val donations = FirebaseFirestore.getInstance().collection("donations")

    /** Asks the Worker to create a PENDING donation. Returns (donationId, payfastUrl). */
    suspend fun startPayment(
        amount: Double, fullName: String, email: String, method: String = "cc"
    ): Pair<String, String> =
        withContext(Dispatchers.IO) {
            val token = FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.await()?.token
                ?: throw Exception("Please sign in again.")

            val body = JSONObject()
                .put("amount", String.format(Locale.US, "%.2f", amount))
                .put("frequency", "once")
                .put("fullName", fullName)
                .put("email", email)
                .put("method", method)   // "cc" credit card or "dc" debit card
                .toString()

            val conn = (URL("$WORKER_URL/create").openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 15_000
                readTimeout = 20_000
                setRequestProperty("Authorization", "Bearer $token")
                setRequestProperty("Content-Type", "application/json")
                doOutput = true
            }
            try {
                conn.outputStream.use { it.write(body.toByteArray()) }
                val ok = conn.responseCode in 200..299
                val text = (if (ok) conn.inputStream else conn.errorStream)
                    ?.bufferedReader()?.use { it.readText() }.orEmpty()
                if (!ok) {
                    val msg = runCatching { JSONObject(text).optString("error") }.getOrNull()
                    throw Exception(if (msg.isNullOrBlank()) "Payment could not be started." else msg)
                }
                val json = JSONObject(text)
                json.getString("donationId") to json.getString("url")
            } finally {
                conn.disconnect()
            }
        }

    /** Calls [onStatus] with the donation's status now and every time it changes. */
    fun watchStatus(donationId: String, onStatus: (String) -> Unit): ListenerRegistration =
        donations.document(donationId).addSnapshotListener { snap, _ ->
            snap?.getString("status")?.let(onStatus)
        }

    /** Admin only (enforced by Firestore security rules). */
    suspend fun getAllDonations(): List<Donation> =
        donations.get().await()
            .documents.mapNotNull { it.toDonation() }
            .sortedByDescending { it.timestamp }
}
