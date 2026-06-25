package com.happyminds.app.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happyminds.app.data.local.Donation
import com.happyminds.app.databinding.ItemAdminDonationBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdminDonationAdapter(
    private val items: List<Donation>
) : RecyclerView.Adapter<AdminDonationAdapter.VH>() {

    inner class VH(private val b: ItemAdminDonationBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(d: Donation) {
            b.tvDonorName.text    = d.fullName
            b.tvAmount.text       = "R${d.amount} · ${d.frequency}"
            b.tvDonationDate.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                .format(Date(d.timestamp))
            // mask card number for display
            b.tvMessage?.text = "Card: **** **** **** ${d.cardNumber.takeLast(4)}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemAdminDonationBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, pos: Int) = holder.bind(items[pos])
    override fun getItemCount() = items.size
}
