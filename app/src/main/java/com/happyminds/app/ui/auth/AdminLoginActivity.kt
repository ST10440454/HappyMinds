package com.happyminds.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.happyminds.app.databinding.ActivityAdminLoginBinding
import com.happyminds.app.ui.admin.AdminDashboardActivity

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnAdminLogin.setOnClickListener {
            val adminId = binding.etAdminId.text.toString().trim()
            val accessKey = binding.etAccessKey.text.toString().trim()

            if (adminId.isEmpty() || accessKey.isEmpty()) {
                Toast.makeText(this, "Please enter Admin ID and Access Key", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Mock admin login
            if (adminId == "ADMIN" && accessKey == "1234") {
                Toast.makeText(this, "Admin access granted! 🛡️", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, AdminDashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBackToUserLogin.setOnClickListener {
            finish()
        }
    }
}
