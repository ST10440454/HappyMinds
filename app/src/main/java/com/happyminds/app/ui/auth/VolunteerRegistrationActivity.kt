package com.happyminds.app.ui.auth

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.happyminds.app.R
import com.happyminds.app.data.User
import com.happyminds.app.data.UserManager
import java.util.UUID

class VolunteerRegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_registration)

        val spinnerHelpType = findViewById<Spinner>(R.id.spinnerHelpType)
        val helpTypes = arrayOf("Teaching Assistant", "Mentor", "Event Coordinator", "Other")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, helpTypes)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spinnerHelpType.adapter = adapter

        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etSkills = findViewById<EditText>(R.id.etSkills)
        val btnRegisterVolunteer = findViewById<Button>(R.id.btnRegisterVolunteer)
        val btnBackToLogin = findViewById<TextView>(R.id.btnBackToLogin)

        btnRegisterVolunteer.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val skills = etSkills.text.toString().trim()
            val helpType = spinnerHelpType.selectedItem.toString()

            if (fullName.isEmpty() || email.isEmpty() || skills.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newVolunteer = User(
                id = UUID.randomUUID().toString(),
                fullName = fullName,
                email = email,
                isVolunteer = true,
                helpType = helpType,
                skills = skills
            )
            UserManager.addUser(newVolunteer)

            Toast.makeText(this, "Volunteer application sent! Please wait for admin approval. 🤝", Toast.LENGTH_LONG).show()
            finish()
        }

        btnBackToLogin.setOnClickListener {
            finish()
        }
    }
}
