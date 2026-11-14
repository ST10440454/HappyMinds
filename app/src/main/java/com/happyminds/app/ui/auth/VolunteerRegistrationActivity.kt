package com.happyminds.app.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.happyminds.app.data.UserRepository
import com.happyminds.app.databinding.ActivityVolunteerRegistrationBinding
import kotlinx.coroutines.launch

/**
 * Volunteer application form.
 *
 * Creates a Firebase Authentication account plus a Firestore profile with isVolunteer = true
 * and status PENDING. The volunteer can only log in after an admin approves the application.
 */
class VolunteerRegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVolunteerRegistrationBinding
    private lateinit var repo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVolunteerRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repo = UserRepository()

        binding.btnSubmit.setOnClickListener { submit() }
        binding.btnBack?.setOnClickListener { finish() }
    }

    private fun submit() {
        val fullName = binding.etFullName.text.toString().trim()
        val email    = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()      // never trim a password
        val helpType = binding.etHelpType.text.toString().trim()
        val skills   = binding.etSkills.text.toString().trim()

        // Clear old errors, then check every field and show the message on the field itself
        listOf(binding.etFullName, binding.etEmail, binding.etPassword,
               binding.etHelpType, binding.etSkills).forEach { it.error = null }

        when {
            fullName.length < 2 -> { binding.etFullName.error = "Enter your full name"; binding.etFullName.requestFocus() }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> { binding.etEmail.error = "Enter a valid email address"; binding.etEmail.requestFocus() }
            password.length < 6 -> { binding.etPassword.error = "Use at least 6 characters"; binding.etPassword.requestFocus() }
            helpType.isEmpty() -> { binding.etHelpType.error = "Tell us how you would like to help"; binding.etHelpType.requestFocus() }
            skills.isEmpty() -> { binding.etSkills.error = "Tell us about your skills or experience"; binding.etSkills.requestFocus() }
            else -> send(fullName, email, password, helpType, skills)
        }
    }

    private fun send(fullName: String, email: String, password: String, helpType: String, skills: String) {
        hideKeyboard()
        setSubmitting(true)

        lifecycleScope.launch {
            val result = repo.register(
                fullName = fullName, email = email, password = password,
                isVolunteer = true, helpType = helpType, skills = skills
            )
            setSubmitting(false)

            result.onSuccess {
                AlertDialog.Builder(this@VolunteerRegistrationActivity)
                    .setTitle("Application submitted ✅")
                    .setMessage(
                        "Thank you for volunteering! An administrator will review your application. " +
                        "You can log in with $email once it has been approved."
                    )
                    .setCancelable(false)
                    .setPositiveButton("OK") { _, _ -> finish() }
                    .show()
            }.onFailure { e ->
                val msg = e.message ?: "Submission failed. Please try again."
                // Show email problems on the email field, everything else as a dialog
                if (msg.contains("email", ignoreCase = true)) {
                    binding.etEmail.error = msg
                    binding.etEmail.requestFocus()
                } else {
                    AlertDialog.Builder(this@VolunteerRegistrationActivity)
                        .setTitle("Could not submit")
                        .setMessage(msg)
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
        }
    }

    private fun setSubmitting(submitting: Boolean) {
        binding.btnSubmit.isEnabled = !submitting
        binding.btnSubmit.text = if (submitting) "Submitting..." else "Submit Application"
    }

    private fun hideKeyboard() {
        currentFocus?.let {
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}
