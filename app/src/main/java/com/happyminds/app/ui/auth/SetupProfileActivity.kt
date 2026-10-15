package com.happyminds.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.happyminds.app.MainActivity
import com.happyminds.app.R
import com.happyminds.app.data.UserRepository
import com.happyminds.app.data.UserSession

class SetupProfileActivity : AppCompatActivity() {

    private lateinit var repo: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile)
        repo = UserRepository(this)

        val etChildName   = findViewById<EditText>(R.id.etChildName)
        val etAge         = findViewById<EditText>(R.id.etAge)
        val spinnerGrade  = findViewById<Spinner>(R.id.spinnerGrade)
        val btnSave       = findViewById<Button>(R.id.btnSaveProfile)

        // Grades 1–7
        val grades = (1..7).map { "Grade $it" }
        val adapter = ArrayAdapter(this, R.layout.spinner_item, grades)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spinnerGrade.adapter = adapter

        // Pre-fill if editing
        UserSession.currentUser?.let {
            etChildName.setText(it.childName)
            etAge.setText(it.age)
            val gradeNum = it.grade.toIntOrNull() ?: 1
            spinnerGrade.setSelection((gradeNum - 1).coerceIn(0, 6))
        }

        btnSave.setOnClickListener {
            val childName = etChildName.text.toString().trim()
            val age       = etAge.text.toString().trim()
            val gradeNum  = (spinnerGrade.selectedItemPosition + 1).toString()

            if (childName.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = UserSession.currentUser?.id ?: return@setOnClickListener
            val result = repo.updateProfile(userId, childName, age, gradeNum)

            result.onSuccess {
                Toast.makeText(this, "Profile saved! 🎉", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }.onFailure {
                Toast.makeText(this, "Failed to save: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
