package com.happyminds.app.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.happyminds.app.data.local.UserDatabase
import com.happyminds.app.data.local.UserProgressEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * UserRepository — persists all data locally using:
 *   • SharedPreferences (JSON)  for user accounts list + logged-in session
 *   • Room DB (per-user file)   for lesson progress
 */
class UserRepository(private val context: Context) {

    private val gson = Gson()
    private val prefs: SharedPreferences =
        context.getSharedPreferences("happyminds_users", Context.MODE_PRIVATE)
    private val sessionPrefs: SharedPreferences =
        context.getSharedPreferences("happyminds_session", Context.MODE_PRIVATE)

    // ── Keys ───────────────────────────────────────────────────────────────
    companion object {
        private const val KEY_USERS = "users_json"
        private const val KEY_CURRENT_USER_ID = "current_user_id"
        const val ADMIN_EMAIL = "HappymindAdmin"
        const val ADMIN_PASSWORD = "Admin1234"
    }

    // ── User list helpers ──────────────────────────────────────────────────
    private fun loadUsers(): MutableList<User> {
        val json = prefs.getString(KEY_USERS, null) ?: return mutableListOf()
        return gson.fromJson(json, object : TypeToken<MutableList<User>>() {}.type)
    }

    private fun saveUsers(users: List<User>) {
        prefs.edit().putString(KEY_USERS, gson.toJson(users)).apply()
    }

    // ── Registration ───────────────────────────────────────────────────────
    fun register(fullName: String, email: String, password: String): Result<User> {
        val users = loadUsers()
        if (users.any { it.email.equals(email, ignoreCase = true) }) {
            return Result.failure(Exception("An account with this email already exists."))
        }
        val newUser = User(
            id = java.util.UUID.randomUUID().toString(),
            fullName = fullName,
            email = email,
            passwordHash = password.hashCode().toString(),
            status = UserStatus.PENDING
        )
        users.add(newUser)
        saveUsers(users)
        return Result.success(newUser)
    }

    // ── Login ──────────────────────────────────────────────────────────────
    fun login(email: String, password: String): Result<User> {
        val users = loadUsers()
        val user = users.find { it.email.equals(email, ignoreCase = true) }
            ?: return Result.failure(Exception("No account found with this email."))
        if (user.passwordHash != password.hashCode().toString()) {
            return Result.failure(Exception("Incorrect password."))
        }
        return when (user.status) {
            UserStatus.PENDING -> Result.failure(Exception("PENDING"))
            UserStatus.REJECTED -> Result.failure(Exception("REJECTED"))
            UserStatus.APPROVED -> {
                sessionPrefs.edit().putString(KEY_CURRENT_USER_ID, user.id).apply()
                UserSession.currentUser = user
                Result.success(user)
            }
        }
    }

    fun logout() {
        sessionPrefs.edit().remove(KEY_CURRENT_USER_ID).apply()
        UserSession.clear()
    }

    // ── Auto-restore session on app restart ────────────────────────────────
    fun restoreSession(): User? {
        val userId = sessionPrefs.getString(KEY_CURRENT_USER_ID, null) ?: return null
        val user = loadUsers().find { it.id == userId } ?: return null
        if (user.status != UserStatus.APPROVED) {
            logout(); return null
        }
        UserSession.currentUser = user
        return user
    }

    // ── Profile update ─────────────────────────────────────────────────────
    fun updateProfile(userId: String, childName: String, age: String, grade: String, fullName: String? = null): Result<User> {
        val users = loadUsers()
        val idx = users.indexOfFirst { it.id == userId }
        if (idx < 0) return Result.failure(Exception("User not found"))
        val updated = users[idx].copy(
            childName = childName, 
            age = age, 
            grade = grade,
            fullName = fullName ?: users[idx].fullName
        )
        users[idx] = updated
        saveUsers(users)
        UserSession.currentUser = updated
        return Result.success(updated)
    }

    // ── Admin helpers ──────────────────────────────────────────────────────
    fun getAllUsers(): List<User> = loadUsers()

    fun updateUserStatus(userId: String, status: UserStatus) {
        val users = loadUsers()
        val idx = users.indexOfFirst { it.id == userId }
        if (idx >= 0) {
            users[idx] = users[idx].copy(status = status)
            saveUsers(users)
        }
    }

    // ── Lesson progress (Room, per-user) ───────────────────────────────────
    private fun progressDao(userId: String) =
        UserDatabase.getInstance(context, userId).progressDao()

    suspend fun getLessonProgress(userId: String, lessonId: String): Int =
        withContext(Dispatchers.IO) {
            progressDao(userId).getProgress(lessonId)?.progressPercent ?: 0
        }

    suspend fun saveProgress(userId: String, lessonId: String, percent: Int) =
        withContext(Dispatchers.IO) {
            progressDao(userId).upsert(
                UserProgressEntity(
                    lessonId = lessonId,
                    progressPercent = percent,
                    lastAccessedAt = System.currentTimeMillis(),
                    completedAt = if (percent >= 100) System.currentTimeMillis() else 0L
                )
            )
            UserSession.updateLessonProgress(lessonId, percent)
        }

    suspend fun getAllProgress(userId: String): Map<String, Int> =
        withContext(Dispatchers.IO) {
            progressDao(userId).getAllProgress().associate { it.lessonId to it.progressPercent }
        }
}
