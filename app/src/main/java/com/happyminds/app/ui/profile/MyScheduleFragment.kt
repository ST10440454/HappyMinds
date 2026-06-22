package com.happyminds.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.happyminds.app.R

class MyScheduleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            findNavController().navigateUp()
        }

        view.findViewById<ImageView>(R.id.btnAddSchedule).setOnClickListener {
            Toast.makeText(requireContext(), "Add to schedule coming soon!", Toast.LENGTH_SHORT).show()
        }

        val days = listOf(R.id.dayMon, R.id.dayTue, R.id.dayWed, R.id.dayThu, R.id.dayFri, R.id.daySat)
        days.forEach { id ->
            view.findViewById<TextView>(id).setOnClickListener { v ->
                // Reset all
                days.forEach { d ->
                    val tv = view.findViewById<TextView>(d)
                    tv.setTextColor(resources.getColor(R.color.text_medium, null))
                    tv.setBackgroundResource(R.drawable.card_background)
                }
                // Highlight selected
                val tv = v as TextView
                tv.setTextColor(resources.getColor(R.color.text_white, null))
                tv.setBackgroundResource(R.drawable.bg_button_orange)
            }
        }
    }
}
