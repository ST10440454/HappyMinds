package com.happyminds.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happyminds.app.R
import com.happyminds.app.data.ClassItem
import com.happyminds.app.databinding.ItemClassBinding

class ClassAdapter(
    private val items: List<ClassItem>,
    private val onJoinClick: (ClassItem) -> Unit = {}
) : RecyclerView.Adapter<ClassAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemClassBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ClassItem) {
            binding.tvClassName.text = item.name
            binding.tvClassTime.text = item.timeRange
            binding.ivSubjectIcon.setImageResource(item.iconRes)
            binding.ivSubjectIcon.setBackgroundResource(
                if (item.isOrange) R.drawable.bg_icon_orange else R.drawable.bg_icon_teal
            )
            binding.btnJoin.setTextColor(
                binding.root.context.getColor(
                    if (item.isOrange) R.color.orange_primary else R.color.teal_primary
                )
            )
            binding.btnJoin.setOnClickListener { onJoinClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
