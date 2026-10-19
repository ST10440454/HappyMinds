package com.happyminds.app.ui.profile

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.happyminds.app.data.UserRepository
import com.happyminds.app.data.UserSession
import com.happyminds.app.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch

/** Lets the signed-in user change their profile details, login email and password. */
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var repo: UserRepository

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repo = UserRepository()

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        UserSession.currentUser?.let { user ->
            binding.tvCurrentEmail.text = "Signed in as ${user.email}"
        }

        binding.btnChangeEmail.setOnClickListener { changeEmail() }
        binding.btnChangePassword.setOnClickListener { changePassword() }
    }

    private fun changeEmail() {
        val newEmail = binding.etNewEmail.text.toString().trim()
        val password = binding.etEmailPassword.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            toast("Enter a valid email address"); return
        }
        if (password.isEmpty()) { toast("Enter your current password"); return }

        binding.btnChangeEmail.isEnabled = false
        viewLifecycleOwner.lifecycleScope.launch {
            val result = repo.changeEmail(password, newEmail)
            _binding?.btnChangeEmail?.isEnabled = true
            result.onSuccess {
                _binding?.etNewEmail?.text?.clear()
                _binding?.etEmailPassword?.text?.clear()
                toast("We sent a verification link to $newEmail. Your login email changes once you open it.")
            }.onFailure { toast(it.message ?: "Could not change email") }
        }
    }

    private fun changePassword() {
        val current = binding.etCurrentPassword.text.toString()
        val new = binding.etNewPassword.text.toString()
        val confirm = binding.etConfirmPassword.text.toString()
        when {
            current.isEmpty() || new.isEmpty() || confirm.isEmpty() -> { toast("Please fill in all password fields"); return }
            new.length < 6 -> { toast("New password must be at least 6 characters"); return }
            new != confirm -> { toast("New passwords don't match"); return }
        }
        binding.btnChangePassword.isEnabled = false
        viewLifecycleOwner.lifecycleScope.launch {
            val result = repo.changePassword(current, new)
            _binding?.btnChangePassword?.isEnabled = true
            result.onSuccess {
                _binding?.etCurrentPassword?.text?.clear()
                _binding?.etNewPassword?.text?.clear()
                _binding?.etConfirmPassword?.text?.clear()
                toast("Password changed ✅")
            }.onFailure { toast(it.message ?: "Could not change password") }
        }
    }

    private fun toast(msg: String) {
        context?.let { Toast.makeText(it, msg, Toast.LENGTH_LONG).show() }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
