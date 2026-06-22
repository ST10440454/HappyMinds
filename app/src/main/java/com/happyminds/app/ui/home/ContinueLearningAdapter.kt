package com.happyminds.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happyminds.app.R
import com.happyminds.app.data.LessonItem
import com.happyminds.app.databinding.ItemContinueLearningBinding

class ContinueLearningAdapter(
    private val items: List<LessonItem>,
    private val onClick: (LessonItem) -> Unit = {}
) : RecyclerView.Adapter<ContinueLearningAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemContinueLearningBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LessonItem) {
            binding.tvLessonName.text = item.name
            binding.progressLesson.progress = item.progressPercent
            binding.tvProgressPct.text = "${item.progressPercent}%"
            binding.ivLessonIcon.setImageResource(item.iconRes)

            if (item.isOrange) {
                binding.root.setCardBackgroundColor(
                    binding.root.context.getColor(R.color.bg_light_orange)
                )
                binding.ivLessonIcon.setBackgroundResource(R.drawable.bg_icon_orange)
                binding.progressLesson.progressDrawable =
                    binding.root.context.getDrawable(R.drawable.progress_bar_orange)
            } else {
                binding.root.setCardBackgroundColor(
                    binding.root.context.getColor(R.color.bg_light_teal)
                )
                binding.ivLessonIcon.setBackgroundResource(R.drawable.bg_icon_teal)
                binding.progressLesson.progressDrawable =
                    binding.root.context.getDrawable(R.drawable.progress_bar_teal)
            }

            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContinueLearningBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
