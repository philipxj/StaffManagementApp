package com.example.staffmanagementapp.data.repository

import com.example.staffmanagementapp.data.api.AuthApiService
import com.example.staffmanagementapp.data.model.dto.LoginRequest
import com.example.staffmanagementapp.data.model.dto.LoginResponse
import com.example.staffmanagementapp.util.Result
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

/**
 * Unit tests for AuthRepositoryImpl
 *
 * This tests the repository layer which is responsible for:
 * - Coordinating between API service and domain models
 * - Error handling and wrapping in Result
 *
 * Based on API specification:
 * - Email: "eve.holt@reqres.in"
 * - Password: "cityslicka"
 * - Expected token: "QpwL5tke4Pnpja7X4"
 */
class AuthRepositoryImplTest {

    private lateinit var mockAuthApiService: MockAuthApiService
    private lateinit var authRepository: AuthRepositoryImpl

    @Before
    fun setup() {
        mockAuthApiService = MockAuthApiService()
        authRepository = AuthRepositoryImpl(mockAuthApiService)
    }

    /**
     * Mock implementation of AuthApiService for testing
     */
    class MockAuthApiService : AuthApiService {

        var shouldThrowException: Boolean = false
        var exceptionToThrow: Exception? = null
        var responseToReturn: LoginResponse? = null
        private var _lastLoginRequest: LoginRequest? = null

        override suspend fun login(loginRequest: LoginRequest): LoginResponse {
            _lastLoginRequest = loginRequest

            if (shouldThrowException && exceptionToThrow != null) {
                throw exceptionToThrow!!
            }

            return responseToReturn ?: throw IllegalStateException("No response configured")
        }

        fun setupSuccessResponse(token: String) {
            shouldThrowException = false
            responseToReturn = LoginResponse(token)
        }

        fun setupExceptionResponse(exception: Exception) {
            shouldThrowException = true
            exceptionToThrow = exception
        }

        fun getLastLoginRequest(): LoginRequest? = _lastLoginRequest
    }

    @Test
    fun `login should return Success with UserToken when API call succeeds`() = runTest {
        // Arrange
        val email = "eve.holt@reqres.in"
        val password = "cityslicka"
        val expectedToken = "QpwL5tke4Pnpja7X4"

        mockAuthApiService.setupSuccessResponse(expectedToken)

        // Act
        val result = authRepository.login(email, password)

        // Assert
        assertTrue("Result should be Success", result is Result.Success)
        val userToken = (result as Result.Success).data
        assertEquals("Token should match expected value", expectedToken, userToken.value)

        // Verify API service was called with correct parameters
        val lastRequest = mockAuthApiService.getLastLoginRequest()
        assertNotNull("LoginRequest should have been created", lastRequest)
        assertEquals("Email should match", email, lastRequest!!.email)
        assertEquals("Password should match", password, lastRequest.password)
    }

    @Test
    fun `login should return Error when API service throws exception`() = runTest {
        // Arrange
        val email = "invalid@email.com"
        val password = "wrongpassword"
        val expectedException = RuntimeException("Network error")

        mockAuthApiService.setupExceptionResponse(expectedException)

        // Act
        val result = authRepository.login(email, password)

        // Assert
        assertTrue("Result should be Error", result is Result.Error)
        val error = (result as Result.Error).exception
        assertEquals("Exception should match expected", expectedException, error)

        // Verify API service was called
        val lastRequest = mockAuthApiService.getLastLoginRequest()
        assertNotNull("LoginRequest should have been created", lastRequest)
    }

}