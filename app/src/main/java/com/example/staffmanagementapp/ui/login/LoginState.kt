package com.example.staffmanagementapp.ui.login

import com.example.staffmanagementapp.data.model.domain.UserToken

/**
 * Represents the different UI states for the login screen.
 */
sealed class LoginState {
    /** Initial state */
    object Initial : LoginState()
    /** Loading state */
    object Loading : LoginState()
    /** Login successful, carrying the user token */
    data class Success(val userToken: UserToken) : LoginState()
    /** Error occurred, carrying an error message */
    data class Error(val message: String) : LoginState()
}

