package com.example.staffmanagementapp.data.api

import com.example.staffmanagementapp.data.model.dto.LoginRequest
import com.example.staffmanagementapp.data.model.dto.LoginResponse
import org.json.JSONObject

/**
 * Implements the AuthApiService interface, specifically handling network requests related to user authentication.
 */
class DefaultAuthApiService(
    httpClientProvider: HttpClientProvider = DefaultHttpClientProvider(),
    baseUrl: String = "https://reqres.in/api"
) : BaseApiService(httpClientProvider, baseUrl), AuthApiService {

    override suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return try {
            val jsonRequest = JSONObject().apply {
                put("email", loginRequest.email)
                put("password", loginRequest.password)
            }

            val response = executeRequest("/login?delay=5", POST, jsonRequest.toString())

            // Parse the JSON response
            val jsonResponse = JSONObject(response)
            LoginResponse(token = jsonResponse.getString("token"))

        } catch (e: Exception) {
            throw RuntimeException("Login failed: ${e.message}", e)
        }
    }
}
