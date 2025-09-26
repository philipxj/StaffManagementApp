package com.example.staffmanagementapp.data.repository

import com.example.staffmanagementapp.data.model.domain.UserToken
import com.example.staffmanagementapp.util.Result

/**
 * A fake implementation of AuthRepository, specifically for testing ViewModels.
 */
class FakeAuthRepository : AuthRepository {

    private var loginResult: Result<UserToken>? = null

    /**
     * Sets the result to be returned on the next login call.
     */
    fun setLoginResult(result: Result<UserToken>) {
        this.loginResult = result
    }

    override suspend fun login(email: String, password: String): Result<UserToken> {
        return loginResult ?: throw IllegalStateException("Login result has not been set for the test.")
    }
}
