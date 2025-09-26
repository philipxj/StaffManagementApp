package com.example.staffmanagementapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.staffmanagementapp.data.model.domain.UserToken
import com.example.staffmanagementapp.data.repository.FakeAuthRepository
import com.example.staffmanagementapp.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for [LoginViewModel].
 *
 * **Focus of Tests:**
 * 1.  Input validation logic (empty values, incorrect format).
 * 2.  Whether the UI state correctly transitions to Success when the Repository returns success.
 * 3.  Whether the UI state correctly transitions to Error when the Repository returns failure.
 * 4.  Whether the Loading state is correctly set at the beginning and end of the request.
 */
@ExperimentalCoroutinesApi
class LoginViewModelTest {

    // This rule ensures that LiveData updates are instantaneous and synchronous, which is convenient for testing.
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel
    private lateinit var fakeRepository: FakeAuthRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // Before the test starts, replace the coroutine's Main dispatcher with our test dispatcher.
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeAuthRepository()
        viewModel = LoginViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        // After the test ends, reset the Main dispatcher.
        Dispatchers.resetMain()
    }

    @Test
    fun `login with empty email should post error state`() = runTest {
        // Arrange
        viewModel.email.value = ""
        viewModel.password.value = "pass123"

        // Act
        viewModel.login()

        // Assert
        val state = viewModel.loginState.value
        assertTrue(state is LoginState.Error)
        assertEquals("Email and password cannot be empty", (state as LoginState.Error).message)
    }

    @Test
    fun `login with invalid password format should post error state`() = runTest {
        // Arrange
        viewModel.email.value = "test@test.com"
        viewModel.password.value = "123" // Incorrect format (too short)

        // Act
        viewModel.login()

        // Assert
        val state = viewModel.loginState.value
        assertTrue(state is LoginState.Error)
        assertEquals("Incorrect password format: only letters and numbers, length 6-10", (state as LoginState.Error).message)
    }

    @Test
    fun `login success should post Success state`() = runTest {
        // Arrange
        val userToken = UserToken("fake-token-123")
        fakeRepository.setLoginResult(Result.Success(userToken))
        viewModel.email.value = "test@test.com"
        viewModel.password.value = "pass123"

        // Act
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val finalState = viewModel.loginState.value
        assertTrue("Expected Success state but was ${finalState?.javaClass?.simpleName}", finalState is LoginState.Success)
        assertEquals(userToken, (finalState as LoginState.Success).userToken)
    }

    @Test
    fun `login failure should post Error state`() = runTest {
        // Arrange
        val errorMessage = "Network connection failed"
        fakeRepository.setLoginResult(Result.Error(Exception(errorMessage)))
        viewModel.email.value = "test@test.com"
        viewModel.password.value = "pass123"

        // Act
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val finalState = viewModel.loginState.value
        assertTrue("Expected Error state but was ${finalState?.javaClass?.simpleName}", finalState is LoginState.Error)
        assertEquals(errorMessage, (finalState as LoginState.Error).message)
    }
}
