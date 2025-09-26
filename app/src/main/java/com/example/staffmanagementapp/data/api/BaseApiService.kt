package com.example.staffmanagementapp.data.api

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * An abstract base class that provides common functionality needed for API service implementations.
 *
 * @param httpClientProvider Provider for creating HTTP connections (default: DefaultHttpClientProvider)
 * @param baseUrl Base URL for API endpoints (default: reqres.in API)
 */
abstract class BaseApiService(
    private val httpClientProvider: HttpClientProvider = DefaultHttpClientProvider(),
    private val baseUrl: String = "https://reqres.in/api"
) {

    protected val POST = "POST"
    protected val GET = "GET"


    /**
     * Executes HTTP request and returns response body as String
     * @param endpoint API endpoint
     * @param method HTTP method (GET, POST, etc.)
     * @param requestBody Optional request body for POST/PUT requests
     * @return Response body as String
     * @throws RuntimeException for HTTP errors or network issues
     */
    protected suspend fun executeRequest(endpoint: String, method: String = GET, requestBody: String? = null): String = withContext(Dispatchers.IO) {
        val connection = createConnection(endpoint, method)

        try {
            // Send request body for POST/PUT requests
            if (requestBody != null && (method == POST || method == "PUT" || method == "PATCH")) {
                connection.outputStream.use { outputStream ->
                    outputStream.write(requestBody.toByteArray(Charsets.UTF_8))
                    outputStream.flush()
                }
            }

            val responseCode = connection.responseCode
            val responseBody = readResponse(connection)

            if (responseCode != 200) {
                throw RuntimeException("HTTP $responseCode: ${connection.responseMessage}. Response: $responseBody")
            }

            responseBody

        } catch (e: Exception) {
            throw RuntimeException("Request failed: ${e.message}", e)
        } finally {
            connection.disconnect()
        }
    }

    /**
     * Creates and configures an HttpsURLConnection.
     * @param endpoint API endpoint
     * @param method HTTP method (GET, POST, etc.)
     * @return Configured HttpsURLConnection
     */
    private fun createConnection(endpoint: String, method: String = GET): HttpsURLConnection {
        val url = URL("$baseUrl$endpoint")
        val connection = httpClientProvider.createConnection(url)

        connection.requestMethod = method
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.setRequestProperty("Accept", "application/json")
        connection.setRequestProperty("x-api-key", "reqres-free-v1")
        connection.connectTimeout = 15000
        connection.readTimeout = 15000

        if (method == "POST" || method == "PUT" || method == "PATCH") {
            connection.doOutput = true
        }

        return connection
    }

    /**
     * @param connection HttpsURLConnection instance.
     * @return String response from the server.
     */
    private fun readResponse(connection: HttpsURLConnection): String {
        // Determine whether to read the normal input stream or the error stream based on the response code
        val inputStream = if (connection.responseCode >= 400) {
            connection.errorStream
        } else {
            connection.inputStream
        }

        val reader = BufferedReader(InputStreamReader(inputStream))
        val response = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }
        reader.close()
        return response.toString()
    }
}
