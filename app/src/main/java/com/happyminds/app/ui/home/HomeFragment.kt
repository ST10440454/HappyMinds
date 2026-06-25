package com.happyminds.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.happyminds.app.R
import com.happyminds.app.data.Curriculum
import com.happyminds.app.data.UserRepository
import com.happyminds.app.data.UserSession
import com.happyminds.app.databinding.FragmentHomeBinding
import com.happyminds.app.ui.learn.LessonAdapter
import com.happyminds.app.ui.learn.LessonPlayerActivity
import com.happyminds.app.ui.community.CommunityUpdateAdapter
import com.happyminds.app.data.CommunityUpdate
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var repo: UserRepository

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repo = UserRepository(requireContext())

        val user  = UserSession.currentUser
        val child = user?.childName?.ifEmpty { user.fullName } ?: "Learner"
        val grade = user?.grade ?: "?"
        binding.tvWelcome.text = "Hi, $child! 👋"
        binding.tvGrade?.text  = "Grade $grade"

        loadHomeData()
        binding.btnGoToLearn?.setOnClickListener {
            findNavController().navigate(R.id.learnFragment)
        }
    }

    override fun onResume() { super.onResume(); loadHomeData() }

    private fun loadHomeData() {
        val user  = UserSession.currentUser ?: return
        val grade = user.grade.toIntOrNull() ?: 1

        lifecycleScope.launch {
            val progressMap = repo.getAllProgress(user.id)
            val all = Curriculum.getLessonsForGrade(grade)
                .map { it.copy(progressPercent = progressMap[it.id] ?: 0) }

            // Continue Learning: 1-99% or 0%
            val inProgress = all.filter { it.progressPercent in 1..99 }
            val notStarted = all.filter { it.progressPercent == 0 }
            val continueDisplay = (inProgress + notStarted).take(4)

            // Completed Lessons: 100%
            val completedLessons = all.filter { it.progressPercent >= 100 }

            val lessonAdapter = { display: List<com.happyminds.app.data.LessonItem> ->
                LessonAdapter(display) { lesson ->
                    startActivity(Intent(requireContext(), LessonPlayerActivity::class.java).apply {
                        putExtra(LessonPlayerActivity.EXTRA_LESSON_ID,   lesson.id)
                        putExtra(LessonPlayerActivity.EXTRA_LESSON_NAME, lesson.name)
                        putExtra(LessonPlayerActivity.EXTRA_NOTES,       lesson.notes)
                        putExtra(LessonPlayerActivity.EXTRA_DESCRIPTION, lesson.description)
                        putExtra(LessonPlayerActivity.EXTRA_DURATION,    lesson.duration)
                    })
                }
            }

            binding.rvContinueLessons?.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                this.adapter = lessonAdapter(continueDisplay)
            }

            binding.rvCompletedLessons?.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                this.adapter = lessonAdapter(completedLessons)
            }

            // Community Updates
            val updates = listOf(
                CommunityUpdate("TN", "Teacher Nomsa", "2 hours ago", "Great job on the math quiz! Keep practicing those tables."),
                CommunityUpdate("SJ", "School Journey", "1 day ago", "Next week is sports week. Don't forget your gear!")
            )
            binding.rvCommunityUpdates?.apply {
                layoutManager = LinearLayoutManager(requireContext())
                this.adapter = CommunityUpdateAdapter(updates)
            }

            val completedCount = all.count { it.progressPercent >= 100 }
            binding.tvLessonsDone?.text  = "$completedCount/${all.size} done"
            binding.progressRing?.progress =
                if (all.isEmpty()) 0 else completedCount * 100 / all.size
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
