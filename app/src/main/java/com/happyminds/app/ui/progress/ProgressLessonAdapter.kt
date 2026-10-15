package com.happyminds.app.ui.progress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happyminds.app.data.LessonItem
import com.happyminds.app.databinding.ItemProgressLessonBinding

class ProgressLessonAdapter(
    private val items: List<LessonItem>
) : RecyclerView.Adapter<ProgressLessonAdapter.VH>() {

    inner class VH(private val b: ItemProgressLessonBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: LessonItem) {
            b.tvName.text         = item.name
            b.tvSubject.text      = item.subject.displayName
            b.tvPct.text          = "${item.progressPercent}%"
            b.progressBar.progress = item.progressPercent
            b.tvStatus.text = when {
                item.progressPercent >= 100 -> "✅ Done"
                item.progressPercent > 0   -> "▶ In Progress"
                else                       -> "○ Not started"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemProgressLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, pos: Int) = holder.bind(items[pos])
    override fun getItemCount() = items.size
}
