package com.happyminds.app.ui.learn

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happyminds.app.R
import com.happyminds.app.data.LessonItem
import com.happyminds.app.data.Subject
import com.happyminds.app.databinding.ItemLessonBinding

class LessonAdapter(
    private var items: List<LessonItem>,
    private val onClick: (LessonItem) -> Unit
) : RecyclerView.Adapter<LessonAdapter.VH>() {

    inner class VH(private val b: ItemLessonBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: LessonItem) {
            b.tvLessonName.text     = item.name
            b.tvLessonDuration.text = item.duration
            b.progressLesson.progress = item.progressPercent
            b.tvProgressPct.text    = "${item.progressPercent}%"

            // Subject icon colour
            val isOrange = item.subject == Subject.MATHS || item.subject == Subject.ENGLISH
            b.ivLessonIcon.setImageResource(R.drawable.ic_book)
            if (isOrange) {
                b.ivLessonIcon.setBackgroundResource(R.drawable.bg_icon_orange)
                b.progressLesson.progressDrawable =
                    b.root.context.getDrawable(R.drawable.progress_bar_orange)
            } else {
                b.ivLessonIcon.setBackgroundResource(R.drawable.bg_icon_teal)
                b.progressLesson.progressDrawable =
                    b.root.context.getDrawable(R.drawable.progress_bar_teal)
            }

            // Subject tag badge
            b.tvSubjectTag?.text = item.subject.displayName

            b.btnAction.text = if (item.progressPercent >= 100)
                b.root.context.getString(R.string.review)
            else if (item.progressPercent > 0)
                b.root.context.getString(R.string.continue_btn)
            else
                b.root.context.getString(R.string.start)

            b.root.setOnClickListener { onClick(item) }
            b.btnAction.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size
    fun updateList(new: List<LessonItem>) { items = new; notifyDataSetChanged() }
}
