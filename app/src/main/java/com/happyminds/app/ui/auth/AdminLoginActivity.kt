package com.happyminds.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.happyminds.app.data.UserRepository
import com.happyminds.app.databinding.ActivityAdminLoginBinding
import com.happyminds.app.ui.admin.AdminDashboardActivity
import kotlinx.coroutines.launch

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminLoginBinding
    private lateinit var repo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repo = UserRepository()

        binding.btnAdminLogin.setOnClickListener {
            val email    = binding.etAdminEmail.text.toString().trim()
            val password = binding.etAdminPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnAdminLogin.isEnabled = false
            lifecycleScope.launch {
                val result = repo.adminLogin(email, password)
                binding.btnAdminLogin.isEnabled = true
                result.onSuccess {
                    startActivity(Intent(this@AdminLoginActivity, AdminDashboardActivity::class.java))
                    finish()
                }.onFailure {
                    Toast.makeText(this@AdminLoginActivity, it.message ?: "Invalid admin credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnBack?.setOnClickListener { finish() }
    }
}