package com.happyminds.app.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

// Manual mapping (instead of toObject) keeps field names stable — Kotlin's
// `isVolunteer` property would otherwise be serialised by Firestore as "volunteer".

internal fun User.toMap(): Map<String, Any> = mapOf(
    "fullName" to fullName,
    "email" to email,
    "childName" to childName,
    "age" to age,
    "grade" to grade,
    "status" to status.name,
    "isVolunteer" to isVolunteer,
    "helpType" to helpType,
    "skills" to skills,
    "createdAt" to createdAt
)

internal fun DocumentSnapshot.toUser(): User? {
    if (!exists()) return null
    return User(
        id = id,
        fullName = getString("fullName") ?: "",
        email = getString("email") ?: "",
        childName = getString("childName") ?: "",
        age = getString("age") ?: "",
        grade = getString("grade") ?: "",
        status = runCatching { UserStatus.valueOf(getString("status") ?: "PENDING") }
            .getOrDefault(UserStatus.PENDING),
        isVolunteer = getBoolean("isVolunteer") ?: false,
        helpType = getString("helpType") ?: "",
        skills = getString("skills") ?: "",
        createdAt = when (val raw = get("createdAt")) {
            is Number -> raw.toLong()
            is Timestamp -> raw.toDate().time
            is String -> raw.toLongOrNull() ?: 0L
            else -> 0L
        }
    )
}

internal fun DocumentSnapshot.toDonation(): Donation? {
    if (!exists()) return null
    return Donation(
        id = id,
        userId = getString("userId") ?: "",
        // The Worker stores the amount as text such as "50.00"; accept a number as well.
        amount = when (val raw = get("amount")) {
            is String -> raw.toDoubleOrNull() ?: 0.0
            is Number -> raw.toDouble()
            else -> 0.0
        },
        frequency = getString("frequency") ?: "once",
        fullName = getString("fullName") ?: "",
        email = getString("email") ?: "",
        status = getString("status") ?: Donation.STATUS_PENDING,
        pfPaymentId = getString("pfPaymentId") ?: "",
        timestamp = when (val raw = get("timestamp")) {
            is Number -> raw.toLong()
            is Timestamp -> raw.toDate().time
            is String -> raw.toLongOrNull() ?: 0L
            else -> 0L
        }
    )
}
