package com.happyminds.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.happyminds.app.MainActivity
import com.happyminds.app.data.User
import com.happyminds.app.data.UserManager
import com.happyminds.app.databinding.ActivityRegisterBinding
import java.util.UUID

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            val fullName = binding.etFullName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Mock registration success
            val newUser = User(
                id = UUID.randomUUID().toString(),
                fullName = fullName,
                email = email
            )
            UserManager.addUser(newUser)

            Toast.makeText(this, "Registration request sent! Please wait for admin approval. 🎉", Toast.LENGTH_LONG).show()
            finish() // Go back to login
        }

        binding.tvLogin.setOnClickListener {
            finish() // Go back to login
        }
    }
}
