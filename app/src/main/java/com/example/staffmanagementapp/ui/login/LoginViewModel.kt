package com.example.staffmanagementapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.staffmanagementapp.data.repository.AuthRepository
import com.example.staffmanagementapp.util.Result
import kotlinx.coroutines.launch

/**
 * Handles business logic and state management for the login screen.
 * @param authRepository Data repository for user authentication.
 */
class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // For two-way data binding with Data Binding
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    // State for UI observation
    private val _loginState = MutableLiveData<LoginState>(LoginState.Initial)
    val loginState: LiveData<LoginState> = _loginState

    /**
     * Executes the login operation.
     */
    fun login() {
        val currentEmail = email.value
        val currentPassword = password.value

        // 1. Input validation
        if (currentEmail.isNullOrBlank() || currentPassword.isNullOrBlank()) {
            _loginState.value = LoginState.Error("Email and password cannot be empty")
            return
        }
        // Validate password format: 6-10 alphanumeric characters
        if (!currentPassword.matches(Regex("^[a-zA-Z0-9]{6,10}$"))) {
            _loginState.value = LoginState.Error("Incorrect password format: only letters and numbers, length 6-10")
            return
        }

        // 2. Execute login logic
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            when (val result = authRepository.login(currentEmail, currentPassword)) {
                is Result.Success -> {
                    _loginState.value = LoginState.Success(result.data) // Assuming result.data is the user or token
                }
                is Result.Error -> {
                    _loginState.value = LoginState.Error(result.exception.message ?: "Login failed, please try again later")
                }
            }
        }
    }

    /**
     * Resets the state to Idle after a one-time event (like an error message) has been handled by the UI.
     */
    fun onStateHandled() {
        _loginState.value = LoginState.Initial
    }
}
