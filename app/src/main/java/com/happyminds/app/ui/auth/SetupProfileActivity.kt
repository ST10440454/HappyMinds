package com.happyminds.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.happyminds.app.MainActivity
import com.happyminds.app.R
import com.happyminds.app.data.UserManager

class SetupProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile)

        val etChildName = findViewById<EditText>(R.id.etChildName)
        val etAge = findViewById<EditText>(R.id.etAge)
        val etGrade = findViewById<EditText>(R.id.etGrade)
        val btnSaveProfile = findViewById<Button>(R.id.btnSaveProfile)

        btnSaveProfile.setOnClickListener {
            val childName = etChildName.text.toString().trim()
            val age = etAge.text.toString().trim()
            val grade = etGrade.text.toString().trim()

            if (childName.isEmpty() || age.isEmpty() || grade.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update current user
            UserManager.currentUser?.let { user ->
                val updatedUser = user.copy(
                    childName = childName,
                    age = age,
                    grade = grade
                )
                UserManager.currentUser = updatedUser
                
                // Update in the list as well (mock database)
                val users = UserManager.getUsers()
                val index = users.indexOfFirst { it.id == user.id }
                if (index != -1) {
                    users[index] = updatedUser
                }
            }

            Toast.makeText(this, "Profile setup complete! 🎉", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
