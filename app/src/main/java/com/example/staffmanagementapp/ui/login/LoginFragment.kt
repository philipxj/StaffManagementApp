package com.example.staffmanagementapp.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.staffmanagementapp.R
import com.example.staffmanagementapp.databinding.FragmentLoginBinding
import com.example.staffmanagementapp.di.Injection

/**
 * UI Controller for the login screen.
 */
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // Lazily initialize LoginViewModel
    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this, Injection.viewModelFactory)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using Data Binding
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the ViewModel and LifecycleOwner to the binding to enable Data Binding
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Observe changes in loginState
        setupObservers()
    }

    private fun setupObservers() {
        loginViewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoginState.Success -> {
                    // Login successful, navigate to the staff list screen with token
                    val action = LoginFragmentDirections.actionLoginFragmentToStaffListFragment(
                        loginToken = state.userToken.value
                    )
                    findNavController().navigate(action)
                }
                is LoginState.Error -> {
                    // Login failed, show an error dialog
                    showErrorDialog(state.message)
                }
                is LoginState.Loading -> {
                    // Hide keyboard when login starts
                    hideKeyboard()
                }
                is LoginState.Initial -> {
                    // No specific action needed here as UI is handled by data binding
                }
            }
        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Login Failed")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = activity?.currentFocus
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding object to prevent memory leaks
        _binding = null
    }
}
