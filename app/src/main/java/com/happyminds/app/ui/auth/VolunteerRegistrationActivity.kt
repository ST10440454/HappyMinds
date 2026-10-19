package com.happyminds.app.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.happyminds.app.data.UserRepository
import com.happyminds.app.databinding.ActivityVolunteerRegistrationBinding
import kotlinx.coroutines.launch

class VolunteerRegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVolunteerRegistrationBinding
    private lateinit var repo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVolunteerRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repo = UserRepository()

        binding.btnSubmit.setOnClickListener {
            val fullName = binding.etFullName.text.toString().trim()
            val email    = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val helpType = binding.etHelpType.text.toString().trim()
            val skills   = binding.etSkills.text.toString().trim()

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Creates a Firebase Auth account + a PENDING volunteer profile awaiting admin approval
            binding.btnSubmit.isEnabled = false
            lifecycleScope.launch {
                val result = repo.register(
                    fullName, email, password,
                    isVolunteer = true, helpType = helpType, skills = skills
                )
                binding.btnSubmit.isEnabled = true
                result.onSuccess {
                    Toast.makeText(this@VolunteerRegistrationActivity, "Volunteer application submitted! ✅", Toast.LENGTH_LONG).show()
                    finish()
                }.onFailure {
                    Toast.makeText(this@VolunteerRegistrationActivity, it.message ?: "Submission failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnBack?.setOnClickListener { finish() }
    }
}