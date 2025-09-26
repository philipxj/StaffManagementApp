package com.example.staffmanagementapp.data.repository

import com.example.staffmanagementapp.data.api.AuthApiService
import com.example.staffmanagementapp.data.model.domain.UserToken
import com.example.staffmanagementapp.data.model.dto.LoginRequest
import com.example.staffmanagementapp.util.Result

/**
 *  Handles authentication operations
 */
class AuthRepositoryImpl(
    private val authApiService: AuthApiService
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<UserToken> {
        return try {
            val loginRequest = LoginRequest(email, password)
            val loginResponse = authApiService.login(loginRequest)
            val userToken = UserToken(loginResponse.token)
            Result.Success(userToken)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
}