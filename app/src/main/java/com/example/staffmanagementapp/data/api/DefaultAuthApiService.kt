package com.example.staffmanagementapp.data.api

import com.example.staffmanagementapp.data.model.dto.LoginRequest
import com.example.staffmanagementapp.data.model.dto.LoginResponse
import org.json.JSONObject
import java.io.OutputStreamWriter
import javax.net.ssl.HttpsURLConnection

/**
 * Implements the AuthApiService interface, specifically handling network requests related to user authentication.
 */
class DefaultAuthApiService(
    httpClientProvider: HttpClientProvider = DefaultHttpClientProvider(),
    baseUrl: String = "https://reqres.in/api"
) : BaseApiService(httpClientProvider, baseUrl), AuthApiService {

    override suspend fun login(loginRequest: LoginRequest): LoginResponse {
        val connection = createConnection("/login?delay=5", POST)
        val responseString: String

        try {
            val jsonRequest = JSONObject().apply {
                put("email", loginRequest.email)
                put("password", loginRequest.password)
            }

            // Using .use will automatically close the stream
            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(jsonRequest.toString())
                writer.flush()
            }

            // Read the response and check the response code
            val responseCode = connection.responseCode
            responseString = readResponse(connection)

            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw Exception("Login failed with response code: $responseCode, message: $responseString")
            }

            // Parse the JSON response
            val jsonResponse = JSONObject(responseString)
            return LoginResponse(token = jsonResponse.getString("token"))

        } finally {
            connection.disconnect()
        }
    }
}
