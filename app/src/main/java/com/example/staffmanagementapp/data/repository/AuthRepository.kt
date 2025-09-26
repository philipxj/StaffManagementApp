package com.example.staffmanagementapp.data.repository

import com.example.staffmanagementapp.data.model.domain.UserToken
import com.example.staffmanagementapp.util.Result

/**
 * Repository interface for authentication operations
 */
interface AuthRepository {
    suspend fun login(email: String, password: String): Result<UserToken>
}