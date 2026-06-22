package com.happyminds.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.happyminds.app.R

import android.widget.TextView
import com.happyminds.app.data.UserManager

class EditProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etChildName = view.findViewById<EditText>(R.id.etChildName)
        val etAge = view.findViewById<EditText>(R.id.etAge)
        val etGrade = view.findViewById<EditText>(R.id.etGrade)
        val tvAvatarInitials = view.findViewById<TextView>(R.id.tvAvatarInitials)

        UserManager.currentUser?.let { user ->
            etChildName.setText(user.childName)
            etAge.setText(user.age)
            etGrade.setText(user.grade)
            val displayName = user.childName.ifEmpty { user.fullName }
            tvAvatarInitials.text = displayName.split(" ").mapNotNull { it.firstOrNull()?.uppercaseChar() }.joinToString("")
        }

        view.findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            findNavController().navigateUp()
        }

        view.findViewById<Button>(R.id.btnSaveProfile).setOnClickListener {
            val childName = etChildName.text.toString().trim()
            val age = etAge.text.toString().trim()
            val grade = etGrade.text.toString().trim()

            if (childName.isEmpty() || age.isEmpty() || grade.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            UserManager.currentUser?.let { user ->
                val updatedUser = user.copy(
                    childName = childName,
                    age = age,
                    grade = grade
                )
                UserManager.currentUser = updatedUser
                
                val users = UserManager.getUsers()
                val index = users.indexOfFirst { it.id == user.id }
                if (index != -1) {
                    users[index] = updatedUser
                }
            }

            Toast.makeText(requireContext(), "Profile saved! 🎉", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }
}
