package com.happyminds.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.happyminds.app.data.UserRepository
import com.happyminds.app.databinding.ActivityAdminLoginBinding
import com.happyminds.app.ui.admin.AdminDashboardActivity

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminLoginBinding
    private lateinit var repo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repo = UserRepository(this)

        binding.btnAdminLogin.setOnClickListener {
            val email    = binding.etAdminEmail.text.toString().trim()
            val password = binding.etAdminPassword.text.toString().trim()

            if (email == UserRepository.ADMIN_EMAIL && password == UserRepository.ADMIN_PASSWORD) {
                startActivity(Intent(this, AdminDashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid admin credentials", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack?.setOnClickListener { finish() }
    }
}
