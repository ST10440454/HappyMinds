package com.happyminds.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.happyminds.app.R
import com.happyminds.app.data.UserManager
import com.happyminds.app.databinding.FragmentProfileBinding
import com.happyminds.app.ui.auth.LoginActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        UserManager.currentUser?.let { user ->
            binding.tvProfileName.text = user.childName.ifEmpty { user.fullName }
            binding.tvProfileDetail.text = "Age ${user.age} · Grade ${user.grade}"
            val displayName = user.childName.ifEmpty { user.fullName }
            binding.tvAvatarInitials.text = displayName.split(" ").mapNotNull { it.firstOrNull()?.uppercaseChar() }.joinToString("")
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.cardEditProfile.setOnClickListener {
            findNavController().navigate(R.id.editProfileFragment)
        }
        binding.cardMySchedule.setOnClickListener {
            findNavController().navigate(R.id.myScheduleFragment)
        }
        binding.cardAchievements.setOnClickListener {
            findNavController().navigate(R.id.achievementsFragment)
        }
        binding.cardNotifications.setOnClickListener {
            findNavController().navigate(R.id.notificationsFragment)
        }
        binding.cardSupport.setOnClickListener {
            findNavController().navigate(R.id.supportFragment)
        }
        binding.cardContactUs.setOnClickListener {
            findNavController().navigate(R.id.contactUsFragment)
        }
        binding.tvSettings.setOnClickListener {
            Toast.makeText(requireContext(), "Settings", Toast.LENGTH_SHORT).show()
        }
        binding.tvSignOut.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
