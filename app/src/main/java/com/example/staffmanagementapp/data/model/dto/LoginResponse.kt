package com.example.staffmanagement.data.model.dto

/**
 * Data Transfer Object (DTO) for receiving successful login API responses.
 *
 * @property token The authentication token obtained upon successful login.
 */
data class LoginResponse(
    val token: String
)
