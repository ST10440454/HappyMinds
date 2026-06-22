package com.happyminds.app.ui.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.happyminds.app.R
import com.happyminds.app.data.Achievement
import com.happyminds.app.data.SubjectProgress
import com.happyminds.app.databinding.FragmentProgressBinding

class ProgressFragment : Fragment() {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSubjectProgress()
        setupAchievements()
    }

    private fun setupSubjectProgress() {
        val subjects = listOf(
            SubjectProgress("Reading", "Level 3", 85, R.drawable.ic_book, isTeal = false),
            SubjectProgress("Mathematics", "Level 2", 72, R.drawable.ic_book, isTeal = true),
            SubjectProgress("Science", "Level 2", 68, R.drawable.ic_video, isTeal = false)
        )
        binding.rvSubjectProgress.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = SubjectProgressAdapter(subjects)
        }
    }

    private fun setupAchievements() {
        val achievements = listOf(
            Achievement("First Lesson", R.drawable.ic_star, isTeal = false),
            Achievement("Week Streak", R.drawable.ic_heart, isTeal = true),
            Achievement("Math Master", R.drawable.ic_award, isTeal = false),
            Achievement("Reader", R.drawable.ic_book, isTeal = false),
            Achievement("Artist", R.drawable.ic_star, isTeal = true),
            Achievement("Scientist", R.drawable.ic_video, isTeal = false)
        )
        binding.rvAchievements.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = AchievementAdapter(achievements)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
