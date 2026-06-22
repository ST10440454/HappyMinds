package com.happyminds.app.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happyminds.app.data.local.Donation
import com.happyminds.app.databinding.ItemAdminDonationBinding
import java.text.SimpleDateFormat
import java.util.*

class AdminDonationAdapter(private var donations: List<Donation>) : RecyclerView.Adapter<AdminDonationAdapter.DonationViewHolder>() {

    fun updateList(newList: List<Donation>) {
        donations = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val binding = ItemAdminDonationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DonationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        holder.bind(donations[position])
    }

    override fun getItemCount() = donations.size

    class DonationViewHolder(private val binding: ItemAdminDonationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(donation: Donation) {
            binding.tvAmount.text = donation.amount
            binding.tvDonorName.text = donation.fullName
            binding.tvDonorEmail.text = donation.email
            binding.tvFrequency.text = donation.frequency
            
            val date = Date(donation.timestamp)
            val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            binding.tvDate.text = format.format(date)
        }
    }
}
