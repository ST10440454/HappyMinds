package com.happyminds.app.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.happyminds.app.R
import com.happyminds.app.data.UserManager
import com.happyminds.app.data.local.Donation
import com.happyminds.app.data.local.UserDatabase
import kotlinx.coroutines.launch

class SupportFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_support, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            findNavController().navigateUp()
        }

        // Initialize Frequency Spinner
        val spinner = view.findViewById<Spinner>(R.id.spinnerFrequency)
        val frequencies = arrayOf("One-time", "Monthly", "Yearly")
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, frequencies)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spinner.adapter = adapter

        view.findViewById<Button>(R.id.btnCompleteDonation).setOnClickListener {
            val amount = view.findViewById<EditText>(R.id.etDonationAmount).text.toString()
            val frequency = spinner.selectedItem.toString()
            val fullName = view.findViewById<EditText>(R.id.etDonationFullName).text.toString()
            val email = view.findViewById<EditText>(R.id.etDonationEmail).text.toString()
            val cardNumber = view.findViewById<EditText>(R.id.etCardNumber).text.toString()
            val cvc = view.findViewById<EditText>(R.id.etCvc).text.toString()

            if (amount.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in the required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save to the user's private database
            UserManager.currentUser?.let { user ->
                lifecycleScope.launch {
                    val userDb = UserDatabase.getInstance(requireContext().applicationContext, user.id)
                    val donation = Donation(
                        amount = amount,
                        frequency = frequency,
                        fullName = fullName,
                        email = email,
                        cardNumber = cardNumber,
                        cvc = cvc
                    )
                    userDb.donationDao().insertDonation(donation)
                    
                    Toast.makeText(requireContext(), "Thank you for your support! ❤️ Information saved.", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            } ?: run {
                Toast.makeText(requireContext(), "Error: User not logged in", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.btnShare).setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Check out Happy Minds!")
                putExtra(Intent.EXTRA_TEXT, "I'm using Happy Minds to learn — you should try it too! 🌟")
            }
            startActivity(Intent.createChooser(shareIntent, "Share Happy Minds"))
        }

        view.findViewById<Button>(R.id.btnRate).setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${requireContext().packageName}")))
            } catch (e: Exception) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${requireContext().packageName}")))
            }
        }
    }
}
