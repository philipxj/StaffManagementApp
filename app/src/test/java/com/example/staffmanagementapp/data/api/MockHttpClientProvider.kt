package com.example.staffmanagementapp.data.api

import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Mock HttpClientProvider for testing purposes
 */
class MockHttpClientProvider(
    private val responseCode: Int = 200,
    private val responseBody: String = ""
) : HttpClientProvider {

    private var lastCreatedConnection: MockHttpsURLConnection? = null
    private var lastUsedUrl: URL? = null

    override fun createConnection(url: URL): HttpsURLConnection {
        lastUsedUrl = url
        val connection = MockHttpsURLConnection(responseCode, responseBody)
        lastCreatedConnection = connection
        return connection
    }

    // Helper method for testing to inspect the last created connection
    fun getLastConnection(): MockHttpsURLConnection? = lastCreatedConnection

    // Helper method for testing to inspect the last used URL
    fun getLastUsedUrl(): URL? = lastUsedUrl
}