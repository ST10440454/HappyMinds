package com.happyminds.app.ui.progress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happyminds.app.R
import com.happyminds.app.data.Achievement
import com.happyminds.app.databinding.ItemAchievementBinding

class AchievementAdapter(
    private val items: List<Achievement>
) : RecyclerView.Adapter<AchievementAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemAchievementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Achievement) {
            binding.tvBadgeName.text = item.name
            binding.ivBadgeIcon.setImageResource(item.iconRes)
            binding.ivBadgeIcon.setBackgroundResource(
                if (item.isTeal) R.drawable.bg_icon_teal else R.drawable.bg_icon_orange
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAchievementBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
