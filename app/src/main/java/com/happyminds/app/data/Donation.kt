package com.happyminds.app.data

/**
 * A donation record stored in Firestore (collection "donations").
 * Only the last 4 digits of the card are kept — the full card number and CVC
 * must never be stored in the database.
 */
data class Donation(
    val id: String = "",
    val userId: String = "",
    val amount: String = "",
    val frequency: String = "Once",
    val fullName: String = "",
    val email: String = "",
    val cardLast4: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
