package com.happyminds.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.happyminds.app.R
import com.happyminds.app.data.UserRepository
import com.happyminds.app.data.UserSession
import com.happyminds.app.databinding.FragmentEditProfileBinding
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var repo: UserRepository

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repo = UserRepository()
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        val grades = (1..7).map { "Grade $it" }
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, grades)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerGrade.adapter = adapter

        UserSession.currentUser?.let { user ->
            binding.etFullName.setText(user.fullName)
            binding.etChildName.setText(user.childName)
            binding.etAge.setText(user.age)
            binding.spinnerGrade.setSelection(((user.grade.toIntOrNull() ?: 1) - 1).coerceIn(0, 6))
        }

        binding.btnSaveProfile.setOnClickListener {
            val user = UserSession.currentUser ?: return@setOnClickListener
            val fullName = binding.etFullName.text.toString().trim()
            val childName = binding.etChildName.text.toString().trim()
            val age = binding.etAge.text.toString().trim()
            val grade = (binding.spinnerGrade.selectedItemPosition + 1).toString()

            if (fullName.isEmpty() || childName.isEmpty() || age.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all profile fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnSaveProfile.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                repo.updateProfile(user.id, childName, age, grade, fullName)
                    .onSuccess {
                        Toast.makeText(requireContext(), "Profile updated ✅", Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }
                    .onFailure {
                        Toast.makeText(requireContext(), "Update failed: ${it.message}", Toast.LENGTH_SHORT).show()
                        _binding?.btnSaveProfile?.isEnabled = true
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
