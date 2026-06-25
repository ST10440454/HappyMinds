package com.happyminds.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val lessonId: String,
    val progressPercent: Int = 0,
    val lastAccessedAt: Long = System.currentTimeMillis(),
    val completedAt: Long = 0L
)
