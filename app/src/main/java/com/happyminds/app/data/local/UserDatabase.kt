package com.happyminds.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Donation::class], version = 4, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun donationDao(): DonationDao

    companion object {
        private val dbInstances = mutableMapOf<String, UserDatabase>()

        fun getInstance(context: Context, userId: String): UserDatabase {
            return synchronized(this) {
                dbInstances.getOrPut(userId) {
                    Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "user_db_$userId.db"
                    ).fallbackToDestructiveMigration().build()
                }
            }
        }
    }
}
