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
import com.happyminds.app.data.DonationRepository
import com.happyminds.app.data.User
import com.happyminds.app.data.UserRepository
import com.happyminds.app.data.UserStatus
import com.happyminds.app.databinding.ActivityAdminDashboardBinding
import kotlinx.coroutines.launch

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var repo: UserRepository
    private val donationRepo = DonationRepository()
    private var learnersList = listOf<User>()
    private var volunteersList = listOf<User>()
    private var currentSection = "users"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repo = UserRepository()

        // The dashboard needs a signed-in admin (Firestore rules enforce this server-side too).
        if (!repo.isSignedIn()) { finish(); return }

        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvVolunteers.layoutManager = LinearLayoutManager(this)
        binding.rvDonations.layoutManager = LinearLayoutManager(this)

        binding.cardTotalUsers.setOnClickListener { showSection("users") }
        binding.cardVolunteersOverview.setOnClickListener { showSection("volunteers") }
        binding.cardDonationsOverview.setOnClickListener { showSection("donations") }
        setupFilters()

        binding.btnBack?.setOnClickListener { finish() }

        refresh()
    }

    /** Loads users and donations from Firestore and redraws the whole dashboard. */
    private fun refresh() {
        lifecycleScope.launch {
            try {
                val allUsers = repo.getAllUsers()
                val donations = donationRepo.getAllDonations()
                val learners = allUsers.filter { !it.isVolunteer }
                val volunteers = allUsers.filter { it.isVolunteer }

                learnersList = learners
                volunteersList = volunteers

                val approvedCount = learners.count { it.status == UserStatus.APPROVED }
                binding.tvTotalUsersCount.text = learners.size.toString()
                binding.tvVolunteersCount.text = volunteers.size.toString()
                val total = donations.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                binding.tvDonationsAmount.text = "R ${String.format("%.2f", total)}"

                binding.tvDonationCount.text = "${donations.size} Donations"

                binding.rvUsers.adapter =
                    AdminUserAdapter(learners.toMutableList()) { user, status -> changeStatus(user, status) }
                binding.rvVolunteers.adapter =
                    AdminVolunteerAdapter(volunteers) { user, status -> changeStatus(user, status) }
                binding.rvDonations.adapter = AdminDonationAdapter(donations)

                if (binding.layoutDetailView.visibility == View.VISIBLE) {
                    showSection(currentSection)
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@AdminDashboardActivity,
                    "Couldn't load data: ${e.message}", Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun changeStatus(user: User, status: UserStatus) {
        lifecycleScope.launch {
            try {
                repo.updateUserStatus(user.id, status)
                val msg = if (status == UserStatus.APPROVED) "✅ ${user.fullName} approved!"
                else "❌ ${user.fullName} rejected."
                Toast.makeText(this@AdminDashboardActivity, msg, Toast.LENGTH_SHORT).show()
                refresh()
            } catch (e: Exception) {
                Toast.makeText(
                    this@AdminDashboardActivity,
                    "Update failed: ${e.message}", Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupFilters() {
        binding.chipUsers.setOnClickListener { showSection("users") }
        binding.chipVolunteers.setOnClickListener { showSection("volunteers") }
        binding.chipDonations.setOnClickListener { showSection("donations") }
    }

    private fun showSection(section: String) {
        currentSection = section
        binding.layoutDashboardOverview.visibility = View.GONE
        binding.layoutDetailView.visibility = View.VISIBLE

        binding.rvUsers.visibility = if (section == "users") View.VISIBLE else View.GONE
        binding.rvVolunteers.visibility = if (section == "volunteers") View.VISIBLE else View.GONE
        binding.rvDonations.visibility = if (section == "donations") View.VISIBLE else View.GONE

        binding.tvDonationCount.visibility = if (section == "donations") View.VISIBLE else View.GONE

        if (section == "volunteers") {
            binding.tvUserCount.visibility = View.VISIBLE
            val appCount = volunteersList.count { it.status == UserStatus.APPROVED }
            binding.tvUserCount.text = "${volunteersList.size} Volunteers ($appCount Approved)"
        } else if (section == "users") {
            binding.tvUserCount.visibility = View.VISIBLE
            val appCount = learnersList.count { it.status == UserStatus.APPROVED }
            binding.tvUserCount.text = "${learnersList.size} Users ($appCount Approved)"
        } else {
            binding.tvUserCount.visibility = View.GONE
        }

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
            binding.tvUserCount.visibility = View.GONE
            binding.tvDonationCount.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Leaving the dashboard ends the admin's Firebase session.
        if (isFinishing) repo.logout()
    }
}
