package com.happyminds.app.data

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull

/**
 * UserRepository — all data lives in Firebase:
 *
 *   • Firebase Authentication  → email/password accounts and the persisted login session
 *   • Cloud Firestore
 *       users/{uid}                    profile + approval status
 *       users/{uid}/progress/{lesson}  per-lesson progress
 *       admins/{uid}                   (create manually) marks an Auth user as an admin
 *
 * Signing in is two steps: Auth verifies the password, then the Firestore profile is
 * checked for admin approval (PENDING / REJECTED users are signed straight back out).
 */
class UserRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val users = db.collection("users")
    private val admins = db.collection("admins")

    companion object {
        private const val TAG = "UserRepository"
        private const val WRITE_TIMEOUT_MS = 8_000L
    }

    // ── Registration ───────────────────────────────────────────────────────
    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        isVolunteer: Boolean = false,
        helpType: String = "",
        skills: String = ""
    ): Result<User> = guard {
        val created = try {
            auth.createUserWithEmailAndPassword(email.trim(), password).await()
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw Exception("Enter a valid email address.")
        }
        val firebaseUser = created.user
            ?: throw IllegalStateException("Account could not be created.")

        val newUser = User(
            id = firebaseUser.uid,
            fullName = fullName,
            email = email.trim().lowercase(),
            status = UserStatus.PENDING,
            isVolunteer = isVolunteer,
            helpType = helpType.trim(),
            skills = skills.trim()
        )
        try {
            users.document(firebaseUser.uid).set(newUser.toMap()).await()
        } catch (e: Exception) {
            // Don't leave an Auth account behind without a profile.
            runCatching { firebaseUser.delete().await() }
            throw e
        }
        // The account must be approved by an admin before it can be used.
        auth.signOut()
        newUser
    }

    // ── Login ──────────────────────────────────────────────────────────────
    suspend fun login(email: String, password: String): Result<User> = guard {
        val uid = auth.signInWithEmailAndPassword(email, password).await().user?.uid
            ?: throw IllegalStateException("Login failed.")

        val user = users.document(uid).get().await().toUser()
        if (user == null) {
            auth.signOut()
            throw Exception("No learner profile exists for this account.")
        }
        when (user.status) {
            UserStatus.PENDING -> { auth.signOut(); throw Exception("PENDING") }
            UserStatus.REJECTED -> { auth.signOut(); throw Exception("REJECTED") }
            UserStatus.APPROVED -> { val synced = syncEmail(user); UserSession.currentUser = synced; synced }
        }
    }

    suspend fun signInWithGoogleIdToken(idToken: String): Result<User> = guard {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = auth.signInWithCredential(credential).await()
        val firebaseUser = authResult.user
            ?: throw IllegalStateException("Google sign-in failed.")

        var user = users.document(firebaseUser.uid).get().await().toUser()
        if (user == null) {
            val newUser = User(
                id = firebaseUser.uid,
                fullName = firebaseUser.displayName ?: "Google User",
                email = (firebaseUser.email ?: "").lowercase(),
                status = UserStatus.APPROVED,
                isVolunteer = false
            )
            users.document(firebaseUser.uid).set(newUser.toMap()).await()
            user = newUser
        }

        when (user.status) {
            UserStatus.PENDING -> { auth.signOut(); throw Exception("PENDING") }
            UserStatus.REJECTED -> { auth.signOut(); throw Exception("REJECTED") }
            UserStatus.APPROVED -> { val synced = syncEmail(user); UserSession.currentUser = synced; synced }
        }
    }

    fun logout() {
        auth.signOut()
        UserSession.clear()
    }

    suspend fun sendPasswordReset(email: String): Result<Unit> = guard {
        auth.sendPasswordResetEmail(email).await()
        Unit
    }

    // ── Auto-restore session on app restart ────────────────────────────────
    /** Firebase Auth persists the login itself; this reloads the Firestore profile. */
    suspend fun restoreSession(): User? {
        val uid = auth.currentUser?.uid ?: return null
        return try {
            val user = users.document(uid).get().await().toUser()
            if (user == null || user.status != UserStatus.APPROVED) {
                logout(); null
            } else {
                val synced = syncEmail(user)
                UserSession.currentUser = synced
                synced
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // Network/other failure: stay signed in, but send the user to the login screen.
            Log.w(TAG, "restoreSession failed", e)
            null
        }
    }

    // ── Profile update ─────────────────────────────────────────────────────
    suspend fun updateProfile(
        userId: String, childName: String, age: String, grade: String, fullName: String? = null
    ): Result<User> = guard {
        val current = UserSession.currentUser?.takeIf { it.id == userId }
            ?: users.document(userId).get().await().toUser()
            ?: throw Exception("User not found")

        val changes = mutableMapOf<String, Any>(
            "childName" to childName, "age" to age, "grade" to grade
        )
        fullName?.let { changes["fullName"] = it }

        // If offline the SDK queues this write and syncs it later, so a timeout is not a failure.
        withTimeoutOrNull(WRITE_TIMEOUT_MS) { users.document(userId).update(changes).await() }

        val updated = current.copy(
            childName = childName, age = age, grade = grade,
            fullName = fullName ?: current.fullName
        )
        UserSession.currentUser = updated
        updated
    }

    // ── Account settings ───────────────────────────────────────────────────
    /** Re-checks the current password first (Firebase requires a recent login for this). */
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> = guard {
        reauthenticate(currentPassword)
        auth.currentUser!!.updatePassword(newPassword).await()
        Unit
    }

    /**
     * Firebase emails a verification link to the NEW address; the login email only changes
     * once the user clicks it. The Firestore copy is synced on the next login/app start.
     */
    suspend fun changeEmail(currentPassword: String, newEmail: String): Result<Unit> = guard {
        reauthenticate(currentPassword)
        auth.currentUser!!.verifyBeforeUpdateEmail(newEmail.trim()).await()
        Unit
    }

    private suspend fun reauthenticate(password: String) {
        val user = auth.currentUser ?: throw Exception("You are signed out. Please log in again.")
        val email = user.email ?: throw Exception("This account has no email address.")
        try {
            user.reauthenticate(EmailAuthProvider.getCredential(email, password)).await()
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw Exception("Current password is incorrect.")
        }
    }

    /** If the verified login email changed, copy it into the Firestore profile. */
    private fun syncEmail(user: User): User {
        val authEmail = auth.currentUser?.email?.lowercase() ?: return user
        if (authEmail == user.email) return user
        users.document(user.id).update("email", authEmail)
            .addOnFailureListener { Log.w(TAG, "email sync failed", it) }
        return user.copy(email = authEmail)
    }

    // ── Admin ──────────────────────────────────────────────────────────────
    /** Signs in with Firebase Auth and verifies the account is listed in admins/{uid}. */
    suspend fun adminLogin(email: String, password: String): Result<Unit> = guard {
        val uid = auth.signInWithEmailAndPassword(email, password).await().user?.uid
            ?: throw IllegalStateException("Login failed.")
        if (!admins.document(uid).get().await().exists()) {
            auth.signOut()
            throw Exception("This account does not have admin access.")
        }
        Unit
    }

    fun isSignedIn() = auth.currentUser != null

    suspend fun getAllUsers(): List<User> =
        users.get().await()
            .documents.mapNotNull { it.toUser() }
            .sortedByDescending { it.createdAt }

    suspend fun updateUserStatus(userId: String, status: UserStatus) {
        users.document(userId).update("status", status.name).await()
    }

    // ── Lesson progress: users/{uid}/progress/{lessonId} ───────────────────
    private fun progress(userId: String) = users.document(userId).collection("progress")

    suspend fun getLessonProgress(userId: String, lessonId: String): Int = try {
        progress(userId).document(lessonId).get().await().getLong("progressPercent")?.toInt() ?: 0
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Log.w(TAG, "getLessonProgress failed", e); 0
    }

    suspend fun getAllProgress(userId: String): Map<String, Int> = try {
        progress(userId).get().await().documents.associate {
            it.id to (it.getLong("progressPercent")?.toInt() ?: 0)
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Log.w(TAG, "getAllProgress failed", e); emptyMap()
    }

    /** Updates the in-memory cache immediately; Firestore queues the write if offline. */
    fun saveProgress(userId: String, lessonId: String, percent: Int) {
        UserSession.updateLessonProgress(lessonId, percent)
        val now = System.currentTimeMillis()
        progress(userId).document(lessonId).set(
            mapOf(
                "progressPercent" to percent,
                "lastAccessedAt" to now,
                "completedAt" to if (percent >= 100) now else 0L
            )
        ).addOnFailureListener { Log.w(TAG, "saveProgress failed", it) }
    }

    // ── Helpers ────────────────────────────────────────────────────────────
    private suspend fun <T> guard(block: suspend () -> T): Result<T> = try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(Exception(friendlyMessage(e)))
    }

    private fun friendlyMessage(e: Exception): String = when (e) {
        is FirebaseAuthUserCollisionException -> "An account with this email already exists."
        is FirebaseAuthWeakPasswordException -> "Password must be at least 6 characters."
        is FirebaseAuthInvalidUserException,
        is FirebaseAuthInvalidCredentialsException -> "Incorrect email or password."
        is FirebaseNetworkException -> "No internet connection. Please try again."
        is FirebaseFirestoreException -> when (e.code) {
            FirebaseFirestoreException.Code.PERMISSION_DENIED ->
                "Missing or insufficient permissions. Please deploy the Firestore security rules in Firebase Console."
            FirebaseFirestoreException.Code.UNAVAILABLE ->
                "Database service unavailable. Please check your internet connection."
            else -> e.message ?: "Database error occurred."
        }
        else -> e.message ?: "Something went wrong."
    }
}
