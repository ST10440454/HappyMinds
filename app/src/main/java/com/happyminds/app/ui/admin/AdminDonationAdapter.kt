package com.happyminds.app.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happyminds.app.data.Donation
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
            b.tvAmount.text       = "R${"%.2f".format(d.amount)}"
            b.tvDonationDate.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                .format(Date(d.timestamp))
            val status = when (d.status) {
                Donation.STATUS_COMPLETE -> "Paid ✅"
                Donation.STATUS_PENDING -> "Awaiting payment ⏳"
                else -> "Not paid ❌ (${d.status.lowercase()})"
            }
            val payfastId = if (d.pfPaymentId.isNotEmpty()) " · PayFast #${d.pfPaymentId}" else ""
            b.tvMessage?.text = "$status$payfastId"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemAdminDonationBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, pos: Int) = holder.bind(items[pos])
    override fun getItemCount() = items.size
}
