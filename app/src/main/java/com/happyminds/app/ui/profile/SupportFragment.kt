package com.happyminds.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.happyminds.app.data.UserSession
import com.happyminds.app.data.local.Donation
import com.happyminds.app.data.local.UserDatabase
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

            lifecycleScope.launch {
                val sharedDb = UserDatabase.getInstance(requireContext(), "shared")
                sharedDb.donationDao().insertDonation(
                    Donation(
                        amount = amount, frequency = frequency,
                        fullName = fullName, email = email,
                        cardNumber = cardNumber, cvc = cvc
                    )
                )
                Toast.makeText(requireContext(), "Thank you for your donation of R$amount! 💛", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
