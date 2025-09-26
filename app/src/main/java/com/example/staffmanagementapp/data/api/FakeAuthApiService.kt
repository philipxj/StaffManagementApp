package com.example.staffmanagementapp.data.api

import com.example.staffmanagementapp.data.model.dto.LoginRequest
import com.example.staffmanagementapp.data.model.dto.LoginResponse
import kotlinx.coroutines.delay
import java.io.IOException

/**
 * A fake implementation of AuthApiService for demo and testing purposes.
 * It does not make any real network requests.
 */
class FakeAuthApiService : AuthApiService {

    // Successful email and token, according to the API documentation
    private val successfulEmail = "eve.holt@reqres.in"

    private val successfulPassword = "cityslicka"
    private val successToken = "QpwL5tke4Pnpja7X4"

    override suspend fun login(loginRequest: LoginRequest): LoginResponse {
        // Simulate network delay to make the loading effect more noticeable
        delay(1500)

        // Simulate success or failure based on the input email
        if (
//            true ||
            loginRequest.email == successfulEmail && loginRequest.password == successfulPassword) {
            // Simulate a successful response
            return LoginResponse(token = successToken)
        } else {
            // Simulate a failed response by throwing an exception similar to a network error
            throw IOException("Error: Invalid credentials")
        }
    }
}
