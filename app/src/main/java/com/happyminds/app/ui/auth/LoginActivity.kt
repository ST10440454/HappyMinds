package com.happyminds.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.happyminds.app.MainActivity
import com.happyminds.app.data.UserRepository
import com.happyminds.app.data.UserSession
import com.happyminds.app.databinding.ActivityLoginBinding
import com.happyminds.app.ui.admin.AdminDashboardActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var repo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repo = UserRepository(this)

        // Auto-restore saved session
        repo.restoreSession()?.let { navigateToMain(it.childName.isEmpty()) }

        binding.btnLogin.setOnClickListener {
            val email    = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = repo.login(email, password)
            result.onSuccess { user ->
                Toast.makeText(this, "Welcome back! 🎉", Toast.LENGTH_SHORT).show()
                navigateToMain(user.childName.isEmpty() || user.grade.isEmpty())
            }.onFailure { e ->
                when (e.message) {
                    "PENDING"  -> Toast.makeText(this, "Your account is pending admin approval.", Toast.LENGTH_LONG).show()
                    "REJECTED" -> Toast.makeText(this, "Your account request was declined.", Toast.LENGTH_LONG).show()
                    else       -> Toast.makeText(this, e.message ?: "Login failed", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvForgotPassword?.setOnClickListener {
            Toast.makeText(this, "Password reset coming soon!", Toast.LENGTH_SHORT).show()
        }

        binding.tvAdminLogin.setOnClickListener {
            startActivity(Intent(this, AdminLoginActivity::class.java))
        }

        binding.tvVolunteerRegister?.setOnClickListener {
            startActivity(Intent(this, VolunteerRegistrationActivity::class.java))
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
