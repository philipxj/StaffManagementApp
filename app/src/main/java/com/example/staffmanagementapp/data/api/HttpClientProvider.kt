package com.example.staffmanagementapp.data.api

import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Interface for providing HTTP connections, facilitating mocking during testing.
 */
interface HttpClientProvider {
    fun createConnection(url: URL): HttpsURLConnection
}

/**
 * Default implementation of the HTTP connection provider.
 */
class DefaultHttpClientProvider : HttpClientProvider {
    override fun createConnection(url: URL): HttpsURLConnection {
        return url.openConnection() as HttpsURLConnection
    }
}