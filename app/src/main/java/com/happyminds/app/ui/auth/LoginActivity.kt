package com.happyminds.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.happyminds.app.MainActivity
import com.happyminds.app.R
import com.happyminds.app.data.UserRepository
import com.happyminds.app.data.needsChildSetup
import com.happyminds.app.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var repo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repo = UserRepository()

        // Auto-restore saved session
        lifecycleScope.launch {
            repo.restoreSession()?.let { navigateToMain(it.needsChildSetup()) }
        }

        binding.btnLogin.setOnClickListener {
            val email    = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnLogin.isEnabled = false
            lifecycleScope.launch {
                val result = repo.login(email, password)
                binding.btnLogin.isEnabled = true
                result.onSuccess { user ->
                    Toast.makeText(this@LoginActivity, "Welcome back! 🎉", Toast.LENGTH_SHORT).show()
                    navigateToMain(user.needsChildSetup())
                }.onFailure { e ->
                    when (e.message) {
                        "PENDING"  -> Toast.makeText(this@LoginActivity, "Your account is pending admin approval.", Toast.LENGTH_LONG).show()
                        "REJECTED" -> Toast.makeText(this@LoginActivity, "Your account request was declined.", Toast.LENGTH_LONG).show()
                        else       -> Toast.makeText(this@LoginActivity, e.message ?: "Login failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvForgotPassword?.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Enter your email above, then tap Forgot password", Toast.LENGTH_SHORT).show()
            } else lifecycleScope.launch {
                repo.sendPasswordReset(email)
                    .onSuccess { Toast.makeText(this@LoginActivity, "If an account exists, a reset email has been sent.", Toast.LENGTH_LONG).show() }
                    .onFailure { Toast.makeText(this@LoginActivity, it.message ?: "Could not send reset email", Toast.LENGTH_LONG).show() }
            }
        }

        binding.tvAdminLogin.setOnClickListener {
            startActivity(Intent(this, AdminLoginActivity::class.java))
        }

        binding.tvVolunteerRegister?.setOnClickListener {
            startActivity(Intent(this, VolunteerRegistrationActivity::class.java))
        }

        binding.btnGoogleLogin.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        val credentialManager = CredentialManager.create(this)

        val webClientId = getString(R.string.default_web_client_id)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity
                )
                handleGoogleSignInResult(result.credential)
            } catch (e: GetCredentialCancellationException) {
                // User cancelled the prompt
            } catch (e: GetCredentialException) {
                Toast.makeText(this@LoginActivity, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, e.message ?: "Google sign-in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleGoogleSignInResult(credential: Credential) {
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken
                binding.btnGoogleLogin.isEnabled = false
                lifecycleScope.launch {
                    val result = repo.signInWithGoogleIdToken(idToken)
                    binding.btnGoogleLogin.isEnabled = true
                    result.onSuccess { user ->
                        Toast.makeText(this@LoginActivity, "Welcome ${user.fullName}! 🎉", Toast.LENGTH_SHORT).show()
                        navigateToMain(user.needsChildSetup())
                    }.onFailure { e ->
                        when (e.message) {
                            "PENDING"  -> Toast.makeText(this@LoginActivity, "Your account is pending admin approval.", Toast.LENGTH_LONG).show()
                            "REJECTED" -> Toast.makeText(this@LoginActivity, "Your account request was declined.", Toast.LENGTH_LONG).show()
                            else       -> Toast.makeText(this@LoginActivity, e.message ?: "Login failed", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } catch (e: GoogleIdTokenParsingException) {
                Toast.makeText(this, "Failed to parse Google ID token", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMain(needsSetup: Boolean) {
        val intent = if (needsSetup)
            Intent(this, SetupProfileActivity::class.java)
        else
            Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
