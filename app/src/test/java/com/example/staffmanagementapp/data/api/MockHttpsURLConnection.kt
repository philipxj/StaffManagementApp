package com.example.staffmanagementapp.data.api

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Mock implementation of HttpsURLConnection for testing purposes
 */
class MockHttpsURLConnection(
    private val mockResponseCode: Int,
    private val mockResponseBody: String
) : HttpsURLConnection(null) {

    private var method = "GET"
    private val requestProperties = mutableMapOf<String, String>()
    private var outputStream: ByteArrayOutputStream? = null
    private var connected = false
    private var _connectTimeout = 0
    private var _readTimeout = 0

    override fun getResponseCode(): Int {
        if (!connected) connect()
        return mockResponseCode
    }

    override fun getInputStream(): InputStream {
        if (!connected) connect()
        return if (mockResponseCode < 400) {
            ByteArrayInputStream(mockResponseBody.toByteArray())
        } else {
            throw java.io.IOException("Server returned HTTP response code: $mockResponseCode")
        }
    }

    override fun getErrorStream(): InputStream? {
        if (!connected) connect()
        return if (mockResponseCode >= 400) {
            ByteArrayInputStream(mockResponseBody.toByteArray())
        } else null
    }

    override fun getOutputStream(): OutputStream {
        if (outputStream == null) {
            outputStream = ByteArrayOutputStream()
        }
        return outputStream!!
    }

    override fun setRequestMethod(method: String) {
        this.method = method
    }

    override fun getRequestMethod(): String = method

    override fun setRequestProperty(key: String, value: String) {
        requestProperties[key] = value
    }

    override fun getRequestProperty(key: String): String? = requestProperties[key]

    override fun setConnectTimeout(timeout: Int) {
        _connectTimeout = timeout
    }

    override fun getConnectTimeout(): Int = _connectTimeout

    override fun setReadTimeout(timeout: Int) {
        _readTimeout = timeout
    }

    override fun getReadTimeout(): Int = _readTimeout

    override fun connect() {
        connected = true
    }

    override fun disconnect() {
        connected = false
        outputStream = null
    }

    override fun usingProxy(): Boolean = false

    override fun setDoOutput(dooutput: Boolean) {
        doOutput = dooutput
    }

    // Implementation of abstract methods from HttpsURLConnection
    override fun getCipherSuite(): String? = null
    override fun getLocalCertificates(): Array<java.security.cert.Certificate>? = null
    override fun getServerCertificates(): Array<java.security.cert.Certificate>? = null
    override fun getPeerPrincipal(): java.security.Principal? = null
    override fun getLocalPrincipal(): java.security.Principal? = null

    // Helper methods for testing
    fun getWrittenData(): String? {
        return outputStream?.toString()
    }

    fun getSetRequestProperties(): Map<String, String> {
        return requestProperties.toMap()
    }
}