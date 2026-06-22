package com.happyminds.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.happyminds.app.R
import com.happyminds.app.data.ClassItem
import com.happyminds.app.data.LessonItem
import com.happyminds.app.data.UserManager
import com.happyminds.app.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        UserManager.currentUser?.let { user ->
            val firstName = (user.childName.ifEmpty { user.fullName }).split(" ").first()
            binding.tvUserName.text = "$firstName!"
        }

        setupTodayClasses()
        setupContinueLearning()
        setupClickListeners()
    }

    private fun setupTodayClasses() {
        val classes = listOf(
            ClassItem("Reading & Writing", "3:00 PM - 4:30 PM", R.drawable.ic_book, isOrange = true),
            ClassItem("Creative Arts", "4:30 PM - 5:30 PM", R.drawable.ic_video, isOrange = false)
        )
        binding.rvTodayClasses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ClassAdapter(classes) { classItem ->
                Toast.makeText(requireContext(), "Joining ${classItem.name}…", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupContinueLearning() {
        val lessons = listOf(
            LessonItem("Math Practice", "20 min", 67, R.drawable.ic_book, isOrange = true),
            LessonItem("Science Fun", "15 min", 33, R.drawable.ic_video, isOrange = false)
        )
        binding.rvContinueLearning.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = ContinueLearningAdapter(lessons) { lesson ->
                Toast.makeText(requireContext(), "Opening ${lesson.name}…", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnBell.setOnClickListener {
            findNavController().navigate(R.id.notificationsFragment)
        }
        binding.tvViewAll.setOnClickListener {
            findNavController().navigate(R.id.myScheduleFragment)
        }
        binding.btnLessons.setOnClickListener {
            findNavController().navigate(R.id.learnFragment)
        }
        binding.btnSchedule.setOnClickListener {
            findNavController().navigate(R.id.myScheduleFragment)
        }
        binding.btnDonate.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_support)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
