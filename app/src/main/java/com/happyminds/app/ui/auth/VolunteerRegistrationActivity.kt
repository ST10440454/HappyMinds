package com.happyminds.app.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.happyminds.app.data.User
import com.happyminds.app.data.UserRepository
import com.happyminds.app.databinding.ActivityVolunteerRegistrationBinding

class VolunteerRegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVolunteerRegistrationBinding
    private lateinit var repo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVolunteerRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repo = UserRepository(this)

        binding.btnSubmit.setOnClickListener {
            val fullName = binding.etFullName.text.toString().trim()
            val email    = binding.etEmail.text.toString().trim()
            val helpType = binding.etHelpType?.text.toString().trim() ?: ""
            val skills   = binding.etSkills?.text.toString().trim() ?: ""

            if (fullName.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Register as a pending volunteer user (password set to a placeholder)
            val result = repo.register(fullName, email, "Volunteer@${System.currentTimeMillis()}")
            result.onSuccess {
                Toast.makeText(this, "Volunteer application submitted! ✅", Toast.LENGTH_LONG).show()
                finish()
            }.onFailure {
                Toast.makeText(this, it.message ?: "Submission failed", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack?.setOnClickListener { finish() }
    }
}
