package com.happyminds.app.ui.learn

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.happyminds.app.R
import com.happyminds.app.data.Curriculum
import com.happyminds.app.data.LessonItem
import com.happyminds.app.data.Subject
import com.happyminds.app.data.UserRepository
import com.happyminds.app.data.UserSession
import com.happyminds.app.databinding.FragmentLearnBinding
import kotlinx.coroutines.launch

class LearnFragment : Fragment() {

    private var _binding: FragmentLearnBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: LessonAdapter
    private lateinit var repo: UserRepository
    private var selectedSubject: Subject? = null
    private var allLessons: List<LessonItem> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentLearnBinding.inflate(inflater, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repo = UserRepository(requireContext())
        setupRecyclerView()
        loadLessons()
        setupSubjectChips()
        setupSearch()
    }

    private fun setupRecyclerView() {
        adapter = LessonAdapter(emptyList()) { lesson -> openLesson(lesson) }
        binding.rvLessons.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@LearnFragment.adapter
        }
    }

    private fun openLesson(lesson: LessonItem) {
        startActivity(Intent(requireContext(), LessonPlayerActivity::class.java).apply {
            putExtra(LessonPlayerActivity.EXTRA_LESSON_ID,   lesson.id)
            putExtra(LessonPlayerActivity.EXTRA_LESSON_NAME, lesson.name)
            putExtra(LessonPlayerActivity.EXTRA_NOTES,       lesson.notes)
            putExtra(LessonPlayerActivity.EXTRA_DESCRIPTION, lesson.description)
            putExtra(LessonPlayerActivity.EXTRA_DURATION,    lesson.duration)
        })
    }

    private fun loadLessons() {
        val grade = UserSession.currentUser?.grade?.toIntOrNull() ?: 1
        binding.tvGradeLabel?.text = "Grade $grade Learning Centre"

        lifecycleScope.launch {
            val userId = UserSession.currentUser?.id ?: return@launch
            val progressMap = repo.getAllProgress(userId)
            allLessons = Curriculum.getLessonsForGrade(grade).map { lesson ->
                lesson.copy(progressPercent = progressMap[lesson.id] ?: 0)
            }
            applyFilter()
        }
    }

    private fun setupSubjectChips() {
        val chips = listOf(
            binding.chipAll        to null,
            binding.chipMaths      to Subject.MATHS,
            binding.chipEnglish    to Subject.ENGLISH,
            binding.chipScience    to Subject.NATURAL_SCIENCE,
            binding.chipLifeSkills to Subject.LIFE_SKILLS
        )
        chips.forEach { (chip, subject) ->
            chip?.setOnClickListener {
                selectedSubject = subject
                chips.forEach { (c, _) ->
                    if (c == chip) {
                        c?.setBackgroundResource(R.drawable.bg_chip_selected)
                        c?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    } else {
                        c?.setBackgroundResource(R.drawable.bg_chip_unselected)
                        c?.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
                    }
                }
                applyFilter()
            }
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) { applyFilter() }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun applyFilter() {
        val query = binding.etSearch.text.toString()
        var filtered = if (selectedSubject == null) allLessons
                       else allLessons.filter { it.subject == selectedSubject }
        if (query.isNotBlank()) {
            filtered = filtered.filter { it.name.contains(query, ignoreCase = true) }
        }
        adapter.updateList(filtered)
        binding.tvEmptyState?.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onResume() { super.onResume(); loadLessons() }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
