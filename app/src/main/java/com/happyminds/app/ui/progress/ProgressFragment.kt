package com.happyminds.app.ui.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.happyminds.app.data.Curriculum
import com.happyminds.app.data.Subject
import com.happyminds.app.data.UserRepository
import com.happyminds.app.data.UserSession
import com.happyminds.app.databinding.FragmentProgressBinding
import kotlinx.coroutines.launch

class ProgressFragment : Fragment() {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!
    private lateinit var repo: UserRepository

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentProgressBinding.inflate(inflater, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repo = UserRepository(requireContext())
        loadProgress()
    }

    override fun onResume() { super.onResume(); loadProgress() }

    private fun loadProgress() {
        val user  = UserSession.currentUser ?: return
        val grade = user.grade.toIntOrNull() ?: 1

        lifecycleScope.launch {
            val progressMap = repo.getAllProgress(user.id)
            val allLessons  = Curriculum.getLessonsForGrade(grade)

            // Overall stats
            val total     = allLessons.size
            val completed = allLessons.count { (progressMap[it.id] ?: 0) >= 100 }
            val avgPct    = if (total == 0) 0 else allLessons.sumOf { progressMap[it.id] ?: 0 } / total

            binding.tvOverallPct.text     = "$avgPct%"
            binding.tvCompleted.text      = "$completed / $total lessons completed"
            binding.progressOverall.progress = avgPct

            // Per-subject breakdown
            val subjectRows = Subject.entries.map { subject ->
                val subjectLessons = allLessons.filter { it.subject == subject }
                val subjectPct     = if (subjectLessons.isEmpty()) 0
                    else subjectLessons.sumOf { progressMap[it.id] ?: 0 } / subjectLessons.size
                val done           = subjectLessons.count { (progressMap[it.id] ?: 0) >= 100 }
                SubjectProgressRow(
                    subject    = subject,
                    percent    = subjectPct,
                    doneLessons= done,
                    total      = subjectLessons.size
                )
            }

            val adapter = SubjectProgressAdapter(subjectRows)
            binding.rvSubjectProgress.apply {
                layoutManager = LinearLayoutManager(requireContext())
                this.adapter  = adapter
            }

            // Individual lesson list - Only show completed lessons
            val lessonRows = allLessons
                .map { it.copy(progressPercent = progressMap[it.id] ?: 0) }
                .filter { it.progressPercent >= 100 }

            val lessonAdapter = ProgressLessonAdapter(lessonRows)
            binding.rvLessonsProgress?.apply {
                layoutManager = LinearLayoutManager(requireContext())
                this.adapter  = lessonAdapter
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
