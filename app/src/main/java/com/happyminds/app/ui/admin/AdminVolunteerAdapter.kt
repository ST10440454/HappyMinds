package com.happyminds.app.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.happyminds.app.R
import com.happyminds.app.data.User
import com.happyminds.app.data.UserStatus
import com.happyminds.app.databinding.ItemAdminVolunteerBinding

class AdminVolunteerAdapter(
    private var volunteers: List<User>,
    private val onAction: (User, UserStatus) -> Unit
) : RecyclerView.Adapter<AdminVolunteerAdapter.VolunteerViewHolder>() {

    fun updateList(newList: List<User>) {
        volunteers = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VolunteerViewHolder {
        val binding = ItemAdminVolunteerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VolunteerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VolunteerViewHolder, position: Int) {
        holder.bind(volunteers[position])
    }

    override fun getItemCount() = volunteers.size

    inner class VolunteerViewHolder(private val binding: ItemAdminVolunteerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(volunteer: User) {
            binding.tvVolunteerName.text = volunteer.fullName
            binding.tvVolunteerEmail.text = volunteer.email
            binding.tvHelpType.text = "Role: ${volunteer.helpType}"
            binding.tvSkills.text = "Skills: ${volunteer.skills}"
            binding.tvVolunteerAvatar.text = volunteer.fullName.split(" ").mapNotNull { it.firstOrNull()?.uppercaseChar() }.joinToString("")
            
            binding.tvStatus.text = volunteer.status.name
            
            when (volunteer.status) {
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

            binding.btnApprove.setOnClickListener { onAction(volunteer, UserStatus.APPROVED) }
            binding.btnReject.setOnClickListener { onAction(volunteer, UserStatus.REJECTED) }
        }
    }
}
