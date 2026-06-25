package com.happyminds.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.happyminds.app.R
import com.happyminds.app.data.Curriculum
import com.happyminds.app.data.UserRepository
import com.happyminds.app.data.UserSession
import com.happyminds.app.databinding.FragmentProfileBinding
import com.happyminds.app.ui.auth.LoginActivity
import com.happyminds.app.ui.auth.SetupProfileActivity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var repo: UserRepository

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repo = UserRepository(requireContext())
        populateProfile()

        // Navigation listeners
        binding.cardEditProfile?.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_editProfile)
        }

        binding.cardMySchedule?.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_mySchedule)
        }

        binding.cardAchievements?.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_achievements)
        }

        binding.cardNotifications?.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_notifications)
        }

        binding.cardSupport?.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_support)
        }

        binding.cardContactUs?.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_contactUs)
        }

        // Sign out
        binding.tvSignOut?.setOnClickListener {
            repo.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onResume() { super.onResume(); populateProfile() }

    private fun populateProfile() {
        val user = UserSession.currentUser ?: return
        val child = user.childName.ifEmpty { user.fullName }
        val initials = child.split(" ").mapNotNull { it.firstOrNull()?.uppercase() }.take(2).joinToString("")

        binding.tvProfileName?.text = child
        binding.tvProfileDetail?.text = buildString {
            if (user.age.isNotEmpty()) append("Age ${user.age}")
            if (user.grade.isNotEmpty()) {
                if (isNotEmpty()) append(" · ")
                append("Grade ${user.grade}")
            }
        }
        binding.tvAvatarInitials?.text = initials.ifEmpty { "?" }

        val grade = user.grade.toIntOrNull() ?: return
        lifecycleScope.launch {
            val progressMap = repo.getAllProgress(user.id)
            val allLessons  = Curriculum.getLessonsForGrade(grade)
            val completed   = allLessons.count { (progressMap[it.id] ?: 0) >= 100 }

            binding.tvStatLessons?.text = "$completed"
            // Badges = completed lessons ÷ 3 (every 3 lessons = 1 badge)
            binding.tvStatBadges?.text = "${completed / 3}"
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
