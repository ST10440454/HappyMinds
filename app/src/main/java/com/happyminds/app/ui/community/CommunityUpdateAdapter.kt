package com.happyminds.app.ui.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happyminds.app.data.CommunityUpdate
import com.happyminds.app.databinding.ItemCommunityUpdateBinding

class CommunityUpdateAdapter(
    private val items: List<CommunityUpdate>
) : RecyclerView.Adapter<CommunityUpdateAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemCommunityUpdateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CommunityUpdate) {
            binding.tvAvatar.text = item.avatarInitials
            binding.tvUpdateTitle.text = item.title
            binding.tvUpdateTime.text = item.timeAgo
            binding.tvUpdateBody.text = item.body
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommunityUpdateBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
