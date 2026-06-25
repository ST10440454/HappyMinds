package com.happyminds.app.ui.progress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happyminds.app.data.Subject
import com.happyminds.app.databinding.ItemSubjectProgressBinding

class SubjectProgressAdapter(
    private val items: List<SubjectProgressRow>
) : RecyclerView.Adapter<SubjectProgressAdapter.VH>() {

    inner class VH(private val b: ItemSubjectProgressBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(row: SubjectProgressRow) {
            b.tvSubjectName.text  = row.subject.displayName
            b.tvSubjectPct.text   = "${row.percent}%"
            b.tvSubjectDone.text  = "${row.doneLessons}/${row.total} lessons"
            b.progressSubject.progress = row.percent
            val emoji = when (row.subject) {
                Subject.MATHS          -> "🔢"
                Subject.ENGLISH        -> "📖"
                Subject.NATURAL_SCIENCE-> "🔬"
                Subject.LIFE_SKILLS    -> "🌱"
            }
            b.tvSubjectEmoji?.text = emoji
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemSubjectProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, pos: Int) = holder.bind(items[pos])
    override fun getItemCount() = items.size
}
