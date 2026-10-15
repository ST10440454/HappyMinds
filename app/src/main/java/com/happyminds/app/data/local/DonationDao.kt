package com.happyminds.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DonationDao {
    @Insert
    suspend fun insertDonation(donation: Donation)

    @Query("SELECT * FROM donations ORDER BY timestamp DESC")
    suspend fun getAllDonations(): List<Donation>
}
