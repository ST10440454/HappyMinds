package com.happyminds.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.happyminds.app.data.UserRepository
import com.happyminds.app.data.UserSession
import com.happyminds.app.databinding.FragmentEditProfileBinding

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
        repo = UserRepository(requireContext())

        UserSession.currentUser?.let { user ->
            binding.etFullName?.setText(user.fullName)
            binding.etChildName?.setText(user.childName)
            binding.etAge?.setText(user.age)
        }

        binding.btnSave.setOnClickListener {
            val user = UserSession.currentUser ?: return@setOnClickListener
            val fullName  = binding.etFullName?.text.toString().trim() ?: user.fullName
            val childName = binding.etChildName?.text.toString().trim() ?: user.childName
            val age       = binding.etAge?.text.toString().trim() ?: user.age

            repo.updateProfile(user.id, childName, age, user.grade, fullName)
                .onSuccess {
                    Toast.makeText(requireContext(), "Profile updated ✅", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
                .onFailure {
                    Toast.makeText(requireContext(), "Update failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
