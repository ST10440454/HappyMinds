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
    private val users: MutableList<User>,
    private val onStatusChange: (User, UserStatus) -> Unit
) : RecyclerView.Adapter<AdminUserAdapter.VH>() {

    inner class VH(private val b: ItemAdminUserBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(user: User) {
            b.tvUserName.text = user.fullName
            b.tvUserEmail.text = user.email
            b.tvUserStatus.text = user.status.name
            b.tvChildInfo.text = if (user.childName.isNotEmpty())
                "${user.childName} · Age ${user.age} · Grade ${user.grade}" else "Profile not set"

            b.tvUserAvatar.text = user.fullName.split(" ")
                .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                .joinToString("")

            when (user.status) {
                UserStatus.PENDING -> {
                    b.tvUserStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.orange_primary))
                    b.tvUserStatus.setBackgroundResource(R.drawable.bg_light_orange)
                    b.layoutActions.visibility = View.VISIBLE
                }
                UserStatus.APPROVED -> {
                    b.tvUserStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.teal_primary))
                    b.tvUserStatus.setBackgroundResource(R.drawable.bg_light_teal)
                    b.layoutActions.visibility = View.GONE
                }
                UserStatus.REJECTED -> {
                    b.tvUserStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.text_medium))
                    b.tvUserStatus.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.divider))
                    b.layoutActions.visibility = View.GONE
                }
            }

            b.root.setOnClickListener {
                b.layoutActions.visibility = if (b.layoutActions.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }

            b.btnApprove.setOnClickListener { onStatusChange(user, UserStatus.APPROVED) }
            b.btnReject.setOnClickListener  { onStatusChange(user, UserStatus.REJECTED)  }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemAdminUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(users[position])
    override fun getItemCount() = users.size
}
