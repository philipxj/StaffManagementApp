package com.example.staffmanagementapp.data.api

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * An abstract base class that provides common functionality needed for API service implementations.
 */
abstract class BaseApiService(
    private val httpClientProvider: HttpClientProvider = DefaultHttpClientProvider(),
    private val baseUrl: String = "https://reqres.in/api"
) {

    protected val POST = "POST"
    protected val GET = "GET"

    /**
     * Creates and configures an HttpsURLConnection.
     * @param endpoint API endpoint
     * @param method HTTP method (GET, POST, etc.)
     * @return Configured HttpsURLConnection
     */
    protected fun createConnection(endpoint: String, method: String = GET): HttpsURLConnection {
        val url = URL("$baseUrl$endpoint")
        val connection = httpClientProvider.createConnection(url)

        connection.requestMethod = method
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.setRequestProperty("Accept", "application/json")
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
    protected fun readResponse(connection: HttpsURLConnection): String {
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
