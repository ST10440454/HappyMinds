package com.happyminds.app.ui.learn

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.happyminds.app.data.UserRepository
import com.happyminds.app.data.UserSession
import com.happyminds.app.databinding.ActivityLessonPlayerBinding
import kotlinx.coroutines.launch

class LessonPlayerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LESSON_ID   = "lesson_id"
        const val EXTRA_LESSON_NAME = "lesson_name"
        const val EXTRA_NOTES       = "notes"
        const val EXTRA_DESCRIPTION = "description"
        const val EXTRA_DURATION    = "duration"
    }

    private lateinit var binding: ActivityLessonPlayerBinding
    private lateinit var repo: UserRepository
    private var lessonId = ""
    private var progress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repo = UserRepository(this)

        lessonId          = intent.getStringExtra(EXTRA_LESSON_ID)   ?: ""
        val name          = intent.getStringExtra(EXTRA_LESSON_NAME) ?: ""
        val dur           = intent.getStringExtra(EXTRA_DURATION)    ?: ""
        val notes         = intent.getStringExtra(EXTRA_NOTES)       ?: ""

        binding.tvLessonTitle.text = name
        binding.tvDuration.text    = "⏱ $dur"
        binding.tvDescription.text = notes.trim()

        // Load saved progress
        val userId = UserSession.currentUser?.id ?: ""
        lifecycleScope.launch {
            progress = repo.getLessonProgress(userId, lessonId)
            binding.progressBar.progress = progress
            binding.tvProgressPct.text   = "$progress% complete"
            updateCompleteButton()
        }

        binding.btnMarkComplete.setOnClickListener {
            progress = 100
            saveProgress(100)
            binding.progressBar.progress  = 100
            binding.tvProgressPct.text    = "100% complete"
            binding.btnMarkComplete.text  = "✅ Completed!"
            binding.btnMarkComplete.isEnabled = false
            Toast.makeText(this, "Well done! Lesson completed 🎉", Toast.LENGTH_SHORT).show()
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun saveProgress(pct: Int) {
        val userId = UserSession.currentUser?.id ?: return
        lifecycleScope.launch { repo.saveProgress(userId, lessonId, pct) }
    }

    private fun updateCompleteButton() {
        if (progress >= 100) {
            binding.btnMarkComplete.text      = "✅ Completed!"
            binding.btnMarkComplete.isEnabled = false
        }
    }
}
