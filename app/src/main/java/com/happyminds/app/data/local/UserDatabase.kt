package com.happyminds.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Per-user Room database.
 * Each user gets their own isolated DB file: user_db_<userId>.db
 * Fresh registrations start with an empty progress table — nothing pre-populated.
 */
@Database(
    entities = [Donation::class, UserProgressEntity::class],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun donationDao(): DonationDao
    abstract fun progressDao(): UserProgressDao

    companion object {
        private val instances = mutableMapOf<String, UserDatabase>()

        fun getInstance(context: Context, userId: String): UserDatabase =
            synchronized(this) {
                instances.getOrPut(userId) {
                    Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "user_db_$userId.db"
                    ).fallbackToDestructiveMigration().build()
                }
            }

        fun closeAll() {
            instances.values.forEach { it.close() }
            instances.clear()
        }
    }
}
