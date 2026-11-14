package com.happyminds.app.data

/**
 * A donation record (Firestore collection "donations").
 *
 * The record is created by the PayFast Cloudflare Worker as PENDING when the donor starts paying,
 * and the Worker sets it to COMPLETE (or FAILED / CANCELLED) only after PayFast's server-to-server
 * notification passes every check. The app never writes donations and never sees card details —
 * the card is entered on PayFast's own payment page.
 *
 * The document id is also PayFast's m_payment_id.
 */
data class Donation(
    val id: String = "",
    val userId: String = "",
    val amount: Double = 0.0,
    val frequency: String = "once",
    val fullName: String = "",
    val email: String = "",
    val status: String = STATUS_PENDING,
    val pfPaymentId: String = "",
    val timestamp: Long = 0L
) {
    val isPaid get() = status == STATUS_COMPLETE

    companion object {
        const val STATUS_PENDING = "PENDING"
        const val STATUS_COMPLETE = "COMPLETE"
        const val STATUS_FAILED = "FAILED"
        const val STATUS_CANCELLED = "CANCELLED"
    }
}
