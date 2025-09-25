package com.example.staffmanagementapp.data.model.dto

/**
 * Data Transfer Object (DTO) for the login API request.
 * The structure of this class directly corresponds to the JSON Request Body required by the API.
 *
 * @property email The user's email address.
 * @property password The user's password.
 */
data class LoginRequest(
    val email: String,
    val password: String
)