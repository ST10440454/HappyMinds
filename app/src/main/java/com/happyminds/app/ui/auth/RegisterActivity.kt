package com.happyminds.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.happyminds.app.data.UserRepository
import com.happyminds.app.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var repo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repo = UserRepository(this)

        binding.btnRegister.setOnClickListener {
            val fullName = binding.etFullName.text.toString().trim()
            val email    = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnRegister.isEnabled = false
            binding.progressBar?.visibility = View.VISIBLE

            val result = repo.register(fullName, email, password)
            binding.btnRegister.isEnabled = true
            binding.progressBar?.visibility = View.GONE

            result.onSuccess {
                Toast.makeText(
                    this,
                    "Registration sent! Please wait for admin approval. 🎉",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }.onFailure {
                Toast.makeText(this, it.message ?: "Registration failed", Toast.LENGTH_LONG).show()
            }
        }

        binding.tvLogin.setOnClickListener { finish() }
    }
}
