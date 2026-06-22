package com.happyminds.app.ui.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.happyminds.app.data.CommunityUpdate
import com.happyminds.app.databinding.FragmentCommunityBinding

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCommunityUpdates()
        setupClickListeners()
    }

    private fun setupCommunityUpdates() {
        val updates = listOf(
            CommunityUpdate(
                avatarInitials = "TN",
                title = "Teacher Nomsa posted a new update",
                timeAgo = "2 hours ago",
                body = "Great work this week, class! Remember to complete your reading assignments by Friday."
            ),
            CommunityUpdate(
                avatarInitials = "PK",
                title = "Principal Khumalo shared an announcement",
                timeAgo = "Yesterday",
                body = "Don't forget – our Family Day Celebration is coming up on June 15th. All families are welcome!"
            ),
            CommunityUpdate(
                avatarInitials = "TM",
                title = "Teacher Mokoena added new Science resources",
                timeAgo = "2 days ago",
                body = "New video lessons on the Water Cycle and Parts of a Plant are now available in the Learning Center."
            )
        )
        binding.rvCommunityUpdates.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = CommunityUpdateAdapter(updates)
        }
    }

    private fun setupClickListeners() {
        binding.tvRsvp.setOnClickListener {
            Toast.makeText(requireContext(), "RSVP sent for Family Day!", Toast.LENGTH_SHORT).show()
        }
        binding.tvLearnMore.setOnClickListener {
            Toast.makeText(requireContext(), "Learn more about Book Donation Drive", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
