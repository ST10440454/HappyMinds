package com.happyminds.app.ui.learn

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happyminds.app.R
import com.happyminds.app.data.LessonItem
import com.happyminds.app.databinding.ItemLessonBinding

class LessonAdapter(
    private var items: List<LessonItem>,
    private val onActionClick: (LessonItem) -> Unit = {}
) : RecyclerView.Adapter<LessonAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemLessonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LessonItem) {
            binding.tvLessonName.text = item.name
            binding.tvLessonDuration.text = item.duration
            binding.progressLesson.progress = item.progressPercent
            binding.tvProgressPct.text = "${item.progressPercent}%"
            binding.ivLessonIcon.setImageResource(item.iconRes)

            if (item.isOrange) {
                binding.ivLessonIcon.setBackgroundResource(R.drawable.bg_icon_orange)
                binding.progressLesson.progressDrawable =
                    binding.root.context.getDrawable(R.drawable.progress_bar_orange)
            } else {
                binding.ivLessonIcon.setBackgroundResource(R.drawable.bg_icon_teal)
                binding.progressLesson.progressDrawable =
                    binding.root.context.getDrawable(R.drawable.progress_bar_teal)
            }

            binding.btnAction.text = if (item.progressPercent >= 100)
                binding.root.context.getString(R.string.review)
            else
                binding.root.context.getString(R.string.continue_btn)

            binding.btnAction.setOnClickListener { onActionClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<LessonItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
