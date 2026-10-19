package com.happyminds.app.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.happyminds.app.data.UserRepository
import com.happyminds.app.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var repo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repo = UserRepository()

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

            lifecycleScope.launch {
                val result = repo.register(fullName, email, password)
                binding.btnRegister.isEnabled = true
                binding.progressBar?.visibility = View.GONE

                result.onSuccess {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration sent! Please wait for admin approval. 🎉",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }.onFailure {
                    Toast.makeText(this@RegisterActivity, it.message ?: "Registration failed", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.tvLogin.setOnClickListener { finish() }
    }
}