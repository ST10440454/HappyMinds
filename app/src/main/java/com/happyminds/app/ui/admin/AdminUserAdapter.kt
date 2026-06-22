package com.happyminds.app.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.happyminds.app.R
import com.happyminds.app.data.User
import com.happyminds.app.data.UserStatus
import com.happyminds.app.databinding.ItemAdminUserBinding

class AdminUserAdapter(
    private var users: List<User>,
    private val onAction: (User, UserStatus) -> Unit
) : RecyclerView.Adapter<AdminUserAdapter.UserViewHolder>() {

    fun updateList(newList: List<User>) {
        users = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemAdminUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size

    inner class UserViewHolder(private val binding: ItemAdminUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            val hasProfile = user.childName.isNotEmpty()
            binding.tvUserName.text = if (hasProfile) user.childName else user.fullName
            binding.tvUserEmail.text = user.email
            binding.tvUserDetail.text = if (hasProfile) {
                "Age ${user.age} · Grade ${user.grade} (Parent: ${user.fullName})"
            } else {
                "Registration Pending Profile Setup"
            }
            val initialsSource = if (hasProfile) user.childName else user.fullName
            binding.tvUserAvatar.text = initialsSource.split(" ").mapNotNull { it.firstOrNull()?.uppercaseChar() }.joinToString("")
            
            binding.tvStatus.text = user.status.name
            
            when (user.status) {
                UserStatus.PENDING -> {
                    binding.tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.orange_primary))
                    binding.tvStatus.setBackgroundResource(R.drawable.bg_light_orange)
                    binding.layoutActions.visibility = View.VISIBLE
                }
                UserStatus.APPROVED -> {
                    binding.tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.teal_primary))
                    binding.tvStatus.setBackgroundResource(R.drawable.bg_light_teal)
                    binding.layoutActions.visibility = View.GONE
                }
                UserStatus.REJECTED -> {
                    binding.tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.text_medium))
                    binding.tvStatus.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.divider))
                    binding.layoutActions.visibility = View.GONE
                }
            }

            binding.btnApprove.setOnClickListener { onAction(user, UserStatus.APPROVED) }
            binding.btnReject.setOnClickListener { onAction(user, UserStatus.REJECTED) }
        }
    }
}
