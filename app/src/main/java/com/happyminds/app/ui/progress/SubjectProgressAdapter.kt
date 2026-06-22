package com.happyminds.app.ui.progress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happyminds.app.R
import com.happyminds.app.data.SubjectProgress
import com.happyminds.app.databinding.ItemSubjectProgressBinding

class SubjectProgressAdapter(
    private val items: List<SubjectProgress>
) : RecyclerView.Adapter<SubjectProgressAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemSubjectProgressBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SubjectProgress) {
            binding.tvSubjectName.text = item.name
            binding.tvLevel.text = item.level
            binding.tvPercent.text = "${item.percent}%"
            binding.progressSubject.progress = item.percent
            binding.ivSubjectIcon.setImageResource(item.iconRes)

            if (item.isTeal) {
                binding.ivSubjectIcon.setBackgroundResource(R.drawable.bg_icon_teal)
                binding.progressSubject.progressDrawable =
                    binding.root.context.getDrawable(R.drawable.progress_bar_teal)
                binding.tvPercent.setTextColor(
                    binding.root.context.getColor(R.color.teal_primary)
                )
            } else {
                binding.ivSubjectIcon.setBackgroundResource(R.drawable.bg_icon_orange)
                binding.progressSubject.progressDrawable =
                    binding.root.context.getDrawable(R.drawable.progress_bar_orange)
                binding.tvPercent.setTextColor(
                    binding.root.context.getColor(R.color.orange_primary)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSubjectProgressBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
