package com.happyminds.app.data

import androidx.annotation.DrawableRes

data class ClassItem(
    val name: String,
    val timeRange: String,
    @DrawableRes val iconRes: Int,
    val isOrange: Boolean = true
)

data class LessonItem(
    val name: String,
    val duration: String,
    val progressPercent: Int,
    @DrawableRes val iconRes: Int,
    val isOrange: Boolean = true
)

data class SubjectProgress(
    val name: String,
    val level: String,
    val percent: Int,
    @DrawableRes val iconRes: Int,
    val isTeal: Boolean = false
)

data class Achievement(
    val name: String,
    @DrawableRes val iconRes: Int,
    val isTeal: Boolean = false
)

data class CommunityUpdate(
    val avatarInitials: String,
    val title: String,
    val timeAgo: String,
    val body: String
)

data class User(
    val id: String,
    val fullName: String, // Parent/Volunteer name
    val email: String,
    val childName: String = "",
    val age: String = "",
    val grade: String = "",
    var status: UserStatus = UserStatus.PENDING,
    val isVolunteer: Boolean = false,
    val helpType: String = "",
    val skills: String = ""
)

enum class UserStatus {
    PENDING, APPROVED, REJECTED
}

object UserManager {
    private var _currentUser: User? = null
    var currentUser: User?
        get() = _currentUser
        set(value) { _currentUser = value }

    private val _users = mutableListOf<User>(
        User("100", "Mpendulo Khumalo", "mpendulo@gmail.com", "Luyanda Khumalo", "10", "4", UserStatus.APPROVED),
        User("1", "Alice Johnson", "alice@gmail.com", "", "8", "3", UserStatus.PENDING),
        User("2", "Bob Smith", "bob@icloud.com", "", "9", "4", UserStatus.PENDING),
        User("3", "Charlie Brown", "charlie@gmail.com", "", "7", "2", UserStatus.APPROVED)
    )

    fun getUsers() = _users

    fun addUser(user: User) {
        _users.add(user)
    }

    fun updateUserStatus(userId: String, status: UserStatus) {
        _users.find { it.id == userId }?.status = status
    }
}
