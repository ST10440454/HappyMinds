package com.happyminds.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.happyminds.app.MainActivity
import com.happyminds.app.data.UserManager
import com.happyminds.app.data.UserStatus
import com.happyminds.app.databinding.ActivityLoginBinding
import com.happyminds.app.ui.admin.AdminDashboardActivity

import androidx.lifecycle.lifecycleScope
import com.happyminds.app.data.local.UserDatabase
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Mock login check
            val user = UserManager.getUsers().find { it.email.equals(email, ignoreCase = true) }
            
            if (user == null) {
                Toast.makeText(this, "Account not found. Please register.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when (user.status) {
                UserStatus.PENDING -> {
                    Toast.makeText(this, "Your account is pending approval.", Toast.LENGTH_LONG).show()
                }
                UserStatus.REJECTED -> {
                    Toast.makeText(this, "Your account request was declined.", Toast.LENGTH_LONG).show()
                }
                UserStatus.APPROVED -> {
                    UserManager.currentUser = user
                    
                    Toast.makeText(this, "Login successful! Welcome back.", Toast.LENGTH_SHORT).show()
                    
                    if (user.childName.isEmpty() || user.age.isEmpty() || user.grade.isEmpty()) {
                        startActivity(Intent(this, SetupProfileActivity::class.java))
                    } else {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    finish()
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Forgot password coming soon!", Toast.LENGTH_SHORT).show()
        }

        binding.tvAdminLogin.setOnClickListener {
            startActivity(Intent(this, AdminLoginActivity::class.java))
        }

        binding.tvVolunteerRegister.setOnClickListener {
            startActivity(Intent(this, VolunteerRegistrationActivity::class.java))
        }
    }
}
