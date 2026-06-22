package com.happyminds.app.ui.admin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.happyminds.app.R
import com.happyminds.app.data.UserManager
import com.happyminds.app.data.UserStatus
import com.happyminds.app.data.local.UserDatabase
import com.happyminds.app.databinding.ActivityAdminDashboardBinding
import kotlinx.coroutines.launch

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var userAdapter: AdminUserAdapter
    private lateinit var donationAdapter: AdminDonationAdapter
    private lateinit var volunteerAdapter: AdminVolunteerAdapter
    
    private enum class Section { OVERVIEW, USERS, DONATIONS, VOLUNTEERS }
    private enum class UserFilter { PENDING, APPROVED }
    
    private var currentSection = Section.OVERVIEW
    private var currentUserFilter = UserFilter.PENDING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViews()
        setupClickListeners()
        updateDashboard()
    }

    private fun setupRecyclerViews() {
        userAdapter = AdminUserAdapter(emptyList()) { user, newStatus ->
            UserManager.updateUserStatus(user.id, newStatus)
            updateDashboard()
        }
        donationAdapter = AdminDonationAdapter(emptyList())
        volunteerAdapter = AdminVolunteerAdapter(emptyList()) { volunteer, newStatus ->
            UserManager.updateUserStatus(volunteer.id, newStatus)
            updateDashboard()
        }
        
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            if (currentSection == Section.OVERVIEW) {
                finish()
            } else {
                currentSection = Section.OVERVIEW
                updateDashboard()
            }
        }

        // Summary Card Clicks
        binding.cardTotalUsers.setOnClickListener {
            currentSection = Section.USERS
            updateDashboard()
        }

        binding.cardDonationsOverview.setOnClickListener {
            currentSection = Section.DONATIONS
            updateDashboard()
        }

        binding.cardVolunteersOverview.setOnClickListener {
            currentSection = Section.VOLUNTEERS
            updateDashboard()
        }

        // Sub filters for Users
        binding.chipPending.setOnClickListener {
            currentUserFilter = UserFilter.PENDING
            updateDashboard()
        }

        binding.chipApproved.setOnClickListener {
            currentUserFilter = UserFilter.APPROVED
            updateDashboard()
        }
    }

    private fun updateDashboard() {
        updateStats()
        updateUIState()
    }

    private fun updateStats() {
        val allUsers = UserManager.getUsers().filter { !it.isVolunteer }
        binding.tvTotalUsersCount.text = allUsers.size.toString()
        
        val allVolunteers = UserManager.getUsers().filter { it.isVolunteer }
        binding.tvVolunteersCount.text = allVolunteers.size.toString()

        lifecycleScope.launch {
            var totalAmount = 0
            UserManager.getUsers().forEach { user ->
                val userDb = UserDatabase.getInstance(applicationContext, user.id)
                val userDonations = userDb.donationDao().getAllDonations()
                userDonations.forEach { totalAmount += it.amount.toDoubleOrNull()?.toInt() ?: 0 }
            }
            binding.tvDonationsAmount.text = "R $totalAmount"
        }
    }

    private fun updateUIState() {
        when (currentSection) {
            Section.OVERVIEW -> {
                binding.layoutDashboardOverview.visibility = View.VISIBLE
                binding.layoutDetailView.visibility = View.GONE
                binding.tvDashboardTitle.text = "Admin Portal"
                binding.tvUserCount.visibility = View.GONE
            }
            Section.USERS -> {
                binding.layoutDashboardOverview.visibility = View.GONE
                binding.layoutDetailView.visibility = View.VISIBLE
                binding.layoutUserSubFilters.visibility = View.VISIBLE
                binding.tvUserCount.visibility = View.VISIBLE
                binding.tvDashboardTitle.text = if (currentUserFilter == UserFilter.PENDING) "Pending Users" else "Approved Users"
                updateUserList()
                updateSubFilterChips()
            }
            Section.DONATIONS -> {
                binding.layoutDashboardOverview.visibility = View.GONE
                binding.layoutDetailView.visibility = View.VISIBLE
                binding.layoutUserSubFilters.visibility = View.GONE
                binding.tvUserCount.visibility = View.VISIBLE
                binding.tvDashboardTitle.text = "Donations"
                updateDonationList()
            }
            Section.VOLUNTEERS -> {
                binding.layoutDashboardOverview.visibility = View.GONE
                binding.layoutDetailView.visibility = View.VISIBLE
                binding.layoutUserSubFilters.visibility = View.GONE
                binding.tvUserCount.visibility = View.VISIBLE
                binding.tvDashboardTitle.text = "Volunteers"
                updateVolunteerList()
            }
        }
    }

    private fun updateSubFilterChips() {
        if (currentUserFilter == UserFilter.PENDING) {
            binding.chipPending.setBackgroundResource(R.drawable.bg_chip_selected)
            binding.chipPending.setTextColor(ContextCompat.getColor(this, R.color.text_white))
            binding.chipApproved.setBackgroundResource(R.drawable.bg_chip_unselected)
            binding.chipApproved.setTextColor(ContextCompat.getColor(this, R.color.text_dark))
        } else {
            binding.chipPending.setBackgroundResource(R.drawable.bg_chip_unselected)
            binding.chipPending.setTextColor(ContextCompat.getColor(this, R.color.text_dark))
            binding.chipApproved.setBackgroundResource(R.drawable.bg_chip_selected)
            binding.chipApproved.setTextColor(ContextCompat.getColor(this, R.color.text_white))
        }
    }

    private fun updateUserList() {
        binding.rvUsers.adapter = userAdapter
        val allUsers = UserManager.getUsers().filter { !it.isVolunteer }
        val filteredUsers = when (currentUserFilter) {
            UserFilter.PENDING -> allUsers.filter { it.status == UserStatus.PENDING }
            UserFilter.APPROVED -> allUsers.filter { it.status == UserStatus.APPROVED }
        }
        userAdapter.updateList(filteredUsers)
        binding.tvUserCount.text = "${filteredUsers.size} Users"
    }

    private fun updateVolunteerList() {
        binding.rvUsers.adapter = volunteerAdapter
        val allVolunteers = UserManager.getUsers().filter { it.isVolunteer }
        volunteerAdapter.updateList(allVolunteers)
        binding.tvUserCount.text = "${allVolunteers.size} Volunteers"
    }

    private fun updateDonationList() {
        binding.rvUsers.adapter = donationAdapter
        lifecycleScope.launch {
            val allDonations = mutableListOf<com.happyminds.app.data.local.Donation>()
            UserManager.getUsers().forEach { user ->
                val userDb = UserDatabase.getInstance(applicationContext, user.id)
                allDonations.addAll(userDb.donationDao().getAllDonations())
            }
            val sortedList = allDonations.sortedByDescending { it.timestamp }
            donationAdapter.updateList(sortedList)
            binding.tvUserCount.text = "${sortedList.size} Donations"
        }
    }
}
