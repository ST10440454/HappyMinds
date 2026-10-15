package com.happyminds.app.data.local

import androidx.room.*

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM user_progress WHERE lessonId = :lessonId")
    suspend fun getProgress(lessonId: String): UserProgressEntity?

    @Query("SELECT * FROM user_progress")
    suspend fun getAllProgress(): List<UserProgressEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(progress: UserProgressEntity)

    @Query("DELETE FROM user_progress")
    suspend fun clearAll()
}
