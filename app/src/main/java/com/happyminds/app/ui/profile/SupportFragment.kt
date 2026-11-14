package com.happyminds.app.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ListenerRegistration
import com.happyminds.app.data.Donation
import com.happyminds.app.data.DonationRepository
import com.happyminds.app.data.UserSession
import com.happyminds.app.databinding.FragmentSupportBinding
import kotlinx.coroutines.launch

/**
 * Donations via PayFast. The donor types their Visa/Mastercard details on PayFast's page, shown
 * inside the app (PayFastCheckoutActivity);
 * the donation only becomes COMPLETE once PayFast's notification reaches our Worker, and this
 * screen listens to the Firestore record to show the result.
 */
class SupportFragment : Fragment() {

    private var _binding: FragmentSupportBinding? = null
    private val binding get() = _binding!!
    private val donations = DonationRepository()
    private var statusListener: ListenerRegistration? = null

    // Result of the in-app PayFast screen. The final outcome still comes from Firestore (watch()).
    private val checkoutLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) toast("Confirming your payment…")
            else toast("Payment cancelled. No money was taken.")
            _binding?.btnDonate?.isEnabled = true
        }

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentSupportBinding.inflate(inflater, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack?.setOnClickListener { findNavController().navigateUp() }

        val user = UserSession.currentUser
        binding.etFullName?.setText(user?.fullName ?: "")
        binding.etEmail?.setText(user?.email ?: "")

        binding.btnDonate?.setOnClickListener { startDonation() }
    }

    // Back from the payment screen (paid or not): let the donor try again.
    override fun onResume() {
        super.onResume()
        _binding?.btnDonate?.isEnabled = true
    }

    private fun startDonation() {
        if (UserSession.currentUser == null) { toast("Please sign in again"); return }
        val amount = binding.etAmount?.text.toString().trim().toDoubleOrNull()
        val fullName = binding.etFullName?.text.toString().trim()
        val email = binding.etEmail?.text.toString().trim()

        if (amount == null || amount < 10 || amount > 50_000) {
            toast("Enter an amount between R10 and R50 000"); return
        }
        if (fullName.isEmpty()) { toast("Please enter your name"); return }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { toast("Enter a valid email address"); return }

        val rounded = Math.round(amount * 100) / 100.0
        val method = if (binding.rbDebit.isChecked) "dc" else "cc"   // card payments only
        binding.btnDonate?.isEnabled = false

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val (donationId, url) = donations.startPayment(rounded, fullName, email, method)
                watch(donationId)
                checkoutLauncher.launch(
                    Intent(requireContext(), PayFastCheckoutActivity::class.java)
                        .putExtra(PayFastCheckoutActivity.EXTRA_URL, url)
                )
            } catch (e: Exception) {
                toast(e.message ?: "Could not start the payment")
                _binding?.btnDonate?.isEnabled = true
            }
        }
    }

    private fun watch(donationId: String) {
        statusListener?.remove()
        statusListener = donations.watchStatus(donationId) { status ->
            when (status) {
                Donation.STATUS_COMPLETE -> {
                    toast("Thank you for your donation! 💛")
                    _binding?.etAmount?.text?.clear()
                    stopWatching()
                }
                Donation.STATUS_FAILED, Donation.STATUS_CANCELLED -> {
                    toast("The payment was not completed. No money was taken.")
                    stopWatching()
                }
            }
        }
    }

    private fun stopWatching() { statusListener?.remove(); statusListener = null }

    private fun toast(msg: String) {
        context?.let { Toast.makeText(it, msg, Toast.LENGTH_LONG).show() }
    }

    override fun onDestroyView() {
        stopWatching()
        super.onDestroyView()
        _binding = null
    }
}
