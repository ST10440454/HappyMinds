package com.happyminds.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "donations")
data class Donation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: String,
    val frequency: String,
    val fullName: String,
    val email: String,
    val cardNumber: String,
    val cvc: String,
    val timestamp: Long = System.currentTimeMillis()
)
