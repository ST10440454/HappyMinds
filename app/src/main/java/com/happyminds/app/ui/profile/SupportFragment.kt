package com.happyminds.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.happyminds.app.R
import com.happyminds.app.data.Donation
import com.happyminds.app.data.DonationRepository
import com.happyminds.app.data.UserSession
import com.happyminds.app.databinding.FragmentSupportBinding
import kotlinx.coroutines.launch

class SupportFragment : Fragment() {

    private var _binding: FragmentSupportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentSupportBinding.inflate(inflater, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack?.setOnClickListener {
            findNavController().navigateUp()
        }

        val frequencies = arrayOf("Once", "Monthly", "Quarterly", "Annually")
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, frequencies)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding.spinnerFrequency?.adapter = adapter

        val user = UserSession.currentUser
        binding.etFullName?.setText(user?.fullName ?: "")
        binding.etEmail?.setText(user?.email ?: "")

        binding.btnDonate?.setOnClickListener {
            val amount     = binding.etAmount?.text.toString().trim()
            val fullName   = binding.etFullName?.text.toString().trim() ?: ""
            val email      = binding.etEmail?.text.toString().trim() ?: ""
            val cardNumber = binding.etCardNumber?.text.toString().trim() ?: ""
            val cvc        = binding.etCvc?.text.toString().trim() ?: ""
            val frequency  = binding.spinnerFrequency?.selectedItem?.toString() ?: "Once"

            if (amount.isEmpty() || cardNumber.isEmpty() || cvc.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnDonate?.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                // Only the last 4 digits are kept — never store the full card number or CVC.
                val synced = try {
                    DonationRepository().addDonation(
                        Donation(
                            userId = user?.id ?: "", amount = amount, frequency = frequency,
                            fullName = fullName, email = email,
                            cardLast4 = cardNumber.filter { it.isDigit() }.takeLast(4)
                        )
                    )
                } catch (e: Exception) {
                    _binding?.btnDonate?.isEnabled = true
                    Toast.makeText(requireContext(), "Donation failed: ${e.message}", Toast.LENGTH_LONG).show()
                    return@launch
                }
                val msg = if (synced) "Thank you for your donation of R$amount! 💛"
                else "Thank you! You're offline, so your donation will be submitted automatically once you reconnect. Please don't submit it again."
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                _binding?.btnDonate?.isEnabled = true
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}