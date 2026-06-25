package com.happyminds.app.ui.admin

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.happyminds.app.R
import com.happyminds.app.data.UserRepository
import com.happyminds.app.data.UserStatus
import com.happyminds.app.databinding.ActivityAdminDashboardBinding
import kotlinx.coroutines.launch

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var repo: UserRepository
    private lateinit var userAdapter: AdminUserAdapter
    private lateinit var volunteerAdapter: AdminVolunteerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repo = UserRepository(this)

        setupOverviewCards()
        setupLists()
        setupFilters()

        binding.btnBack?.setOnClickListener { finish() }
    }

    private fun setupOverviewCards() {
        val allUsers = repo.getAllUsers()
        val learners = allUsers.filter { !it.isVolunteer }
        val volunteers = allUsers.filter { it.isVolunteer }
        
        binding.tvTotalUsersCount.text = learners.size.toString()
        binding.tvVolunteersCount.text = volunteers.size.toString()
        
        // Setup donations total
        lifecycleScope.launch {
            try {
                val sharedDb = com.happyminds.app.data.local.UserDatabase.getInstance(this@AdminDashboardActivity, "shared")
                val donations = sharedDb.donationDao().getAllDonations()
                val total = donations.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                binding.tvDonationsAmount.text = "R ${String.format("%.2f", total)}"
            } catch (_: Exception) {}
        }

        binding.cardTotalUsers.setOnClickListener { showSection("users") }
        binding.cardVolunteersOverview.setOnClickListener { showSection("volunteers") }
        binding.cardDonationsOverview.setOnClickListener { showSection("donations") }
    }

    private fun setupLists() {
        // Users list
        val learners = repo.getAllUsers().filter { !it.isVolunteer }
        userAdapter = AdminUserAdapter(learners.toMutableList()) { user, status ->
            repo.updateUserStatus(user.id, status)
            val msg = if (status == UserStatus.APPROVED) "✅ ${user.fullName} approved!" 
                      else "❌ ${user.fullName} rejected."
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            setupLists() // refresh
            setupOverviewCards()
        }
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(this@AdminDashboardActivity)
            adapter = userAdapter
        }

        // Volunteers list
        val volunteers = repo.getAllUsers().filter { it.isVolunteer }
        volunteerAdapter = AdminVolunteerAdapter(volunteers) { volunteer, status ->
            repo.updateUserStatus(volunteer.id, status)
            val msg = if (status == UserStatus.APPROVED) "✅ ${volunteer.fullName} approved!" 
                      else "❌ ${volunteer.fullName} rejected."
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            setupLists() // refresh
            setupOverviewCards()
        }
        binding.rvVolunteers.apply {
            layoutManager = LinearLayoutManager(this@AdminDashboardActivity)
            adapter = volunteerAdapter
        }

        // Donations list
        lifecycleScope.launch {
            try {
                val sharedDb = com.happyminds.app.data.local.UserDatabase.getInstance(this@AdminDashboardActivity, "shared")
                val donations = sharedDb.donationDao().getAllDonations()
                val adapter = AdminDonationAdapter(donations)
                binding.rvDonations.apply {
                    layoutManager = LinearLayoutManager(this@AdminDashboardActivity)
                    this.adapter = adapter
                }
            } catch (_: Exception) {}
        }
    }

    private fun setupFilters() {
        binding.chipUsers.setOnClickListener { showSection("users") }
        binding.chipVolunteers.setOnClickListener { showSection("volunteers") }
        binding.chipDonations.setOnClickListener { showSection("donations") }
    }

    private fun showSection(section: String) {
        binding.layoutDashboardOverview.visibility = View.GONE
        binding.layoutDetailView.visibility = View.VISIBLE
        
        binding.rvUsers.visibility = if (section == "users") View.VISIBLE else View.GONE
        binding.rvVolunteers.visibility = if (section == "volunteers") View.VISIBLE else View.GONE
        binding.rvDonations.visibility = if (section == "donations") View.VISIBLE else View.GONE
        
        updateChips(section)
    }

    private fun updateChips(selected: String) {
        val activeBg = R.drawable.bg_chip_selected
        val inactiveBg = R.drawable.bg_chip_unselected
        val activeText = ContextCompat.getColor(this, R.color.text_white)
        val inactiveText = ContextCompat.getColor(this, R.color.text_dark)

        fun setChipState(view: TextView, isActive: Boolean) {
            view.setBackgroundResource(if (isActive) activeBg else inactiveBg)
            view.setTextColor(if (isActive) activeText else inactiveText)
        }

        setChipState(binding.chipUsers, selected == "users")
        setChipState(binding.chipVolunteers, selected == "volunteers")
        setChipState(binding.chipDonations, selected == "donations")
    }

    override fun onBackPressed() {
        if (binding.layoutDetailView.visibility == View.VISIBLE) {
            binding.layoutDetailView.visibility = View.GONE
            binding.layoutDashboardOverview.visibility = View.VISIBLE
        } else {
            super.onBackPressed()
        }
    }
}
