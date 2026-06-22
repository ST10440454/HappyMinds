package com.happyminds.app.ui.learn

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.happyminds.app.R
import com.happyminds.app.data.LessonItem
import com.happyminds.app.databinding.FragmentLearnBinding

class LearnFragment : Fragment() {

    private var _binding: FragmentLearnBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: LessonAdapter
    private var selectedCategory = "All"

    private val allLessons = listOf(
        LessonItem("Phonics Basics", "15 min", 100, R.drawable.ic_book, isOrange = true),
        LessonItem("Addition & Subtraction", "20 min", 75, R.drawable.ic_book, isOrange = true),
        LessonItem("Parts of a Plant", "18 min", 40, R.drawable.ic_video, isOrange = false),
        LessonItem("Creative Writing", "25 min", 60, R.drawable.ic_book, isOrange = true),
        LessonItem("Multiplication Tables", "20 min", 30, R.drawable.ic_book, isOrange = true),
        LessonItem("The Water Cycle", "15 min", 0, R.drawable.ic_video, isOrange = false),
        LessonItem("Art & Colour", "30 min", 0, R.drawable.ic_star, isOrange = false),
        LessonItem("Reading Comprehension", "20 min", 85, R.drawable.ic_book, isOrange = true)
    )

    private val categoryMap = mapOf(
        "All" to allLessons,
        "Reading" to allLessons.filter { it.name.contains("Read") || it.name.contains("Writing") || it.name.contains("Phonics") },
        "Math" to allLessons.filter { it.name.contains("Addition") || it.name.contains("Multiplication") },
        "Science" to allLessons.filter { it.name.contains("Plant") || it.name.contains("Water") },
        "Arts" to allLessons.filter { it.name.contains("Art") || it.name.contains("Creative") }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLearnBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupChips()
        setupSearch()
    }

    private fun setupRecyclerView() {
        adapter = LessonAdapter(allLessons) { lesson ->
            Toast.makeText(requireContext(), "Opening: ${lesson.name}", Toast.LENGTH_SHORT).show()
        }
        binding.rvLessons.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@LearnFragment.adapter
        }
    }

    private fun setupChips() {
        val chips = listOf(
            binding.chipAll to "All",
            binding.chipReading to "Reading",
            binding.chipMath to "Math",
            binding.chipScience to "Science",
            binding.chipArts to "Arts"
        )
        chips.forEach { (chip, category) ->
            chip.setOnClickListener {
                selectedCategory = category
                updateChipSelection(chips, chip)
                filterLessons(binding.etSearch.text.toString(), category)
            }
        }
    }

    private fun updateChipSelection(chips: List<Pair<TextView, String>>, selected: TextView) {
        chips.forEach { (chip, _) ->
            if (chip == selected) {
                chip.setBackgroundResource(R.drawable.bg_chip_selected)
                chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_white))
            } else {
                chip.setBackgroundResource(R.drawable.bg_chip_unselected)
                chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_dark))
            }
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterLessons(s.toString(), selectedCategory)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterLessons(query: String, category: String) {
        val base = categoryMap[category] ?: allLessons
        val filtered = if (query.isBlank()) base
        else base.filter { it.name.contains(query, ignoreCase = true) }
        adapter.updateList(filtered)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
