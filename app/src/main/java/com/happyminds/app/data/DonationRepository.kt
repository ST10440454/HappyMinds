package com.happyminds.app.data

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await


fun Donation.toMap(): Map<String, Any> = mapOf(
    "id" to id,
    "userId" to userId,
    "amount" to amount,
    "frequency" to frequency,
    "fullName" to fullName,
    "email" to email,
    "cardLast4" to cardLast4,
    "timestamp" to timestamp
)

fun DocumentSnapshot.toDonation(): Donation? {
    return try {
        val donation = toObject(Donation::class.java) ?: return null
        donation.copy(id = id)
    } catch (e: Exception) {
        null
    }
}

class DonationRepository {

    private val db = FirebaseFirestore.getInstance()
    private val donationsCollection = db.collection("donations")

    companion object {
        private const val TAG = "DonationRepository"
    }

    suspend fun addDonation(donation: Donation): Boolean {
        val docRef = donationsCollection.document()
        val donationWithId = donation.copy(id = docRef.id)
        docRef.set(donationWithId.toMap()).await()
        return true
    }

    suspend fun getAllDonations(): List<Donation> = try {
        donationsCollection.get().await().documents.mapNotNull { it.toDonation() }
            .sortedByDescending { it.timestamp }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Log.w(TAG, "getAllDonations failed", e)
        emptyList()
    }
}
