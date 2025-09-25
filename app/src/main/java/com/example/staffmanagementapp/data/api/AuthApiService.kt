package com.example.staffmanagementapp.data.api

import com.example.staffmanagementapp.data.model.dto.LoginRequest
import com.example.staffmanagementapp.data.model.dto.LoginResponse

/**
 * Interface for authentication API operations
 */
interface AuthApiService {
    suspend fun login(loginRequest: LoginRequest): LoginResponse
}