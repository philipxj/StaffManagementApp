package com.example.staffmanagementapp.ui.staff

import com.example.staffmanagementapp.data.repository.FakeStaffRepository
import com.example.staffmanagementapp.ui.stafflist.StaffListViewModel
import com.example.staffmanagementapp.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for StaffListViewModel.
 *
 * Test Focus:
 * - Initial Load State Transitions
 * - Load More Logic
 * - Error Handling
 * - Last Page State
 */
@ExperimentalCoroutinesApi
class StaffListViewModelTest {

    // This Rule sets the test Coroutine Dispatcher as the main Dispatcher
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var staffListViewModel: StaffListViewModel
    private lateinit var fakeStaffRepository: FakeStaffRepository

    @Before
    fun setup() {
        // Recreate a clean FakeRepository and ViewModel before each test
        fakeStaffRepository = FakeStaffRepository()
        staffListViewModel = StaffListViewModel(fakeStaffRepository)
    }

    @Test
    fun `loadInitialStaffList should update state with staff list on success`() = runTest {
        // Act
        staffListViewModel.loadInitialStaffList()

        // Assert
        val uiState = staffListViewModel.uiState.value
        Assert.assertEquals("Staff list should have 2 items", 2, uiState.staff.size)
        Assert.assertEquals("First staff member's ID should be 1", 1, uiState.staff[0].id)
        Assert.assertFalse(
            "isLoadingInitial should be false after loading",
            uiState.isLoadingInitial
        )
        Assert.assertNull("Error should be null on success", uiState.error)
    }

    @Test
    fun `loadInitialStaffList should update state with error on failure`() = runTest {
        // Arrange
        fakeStaffRepository.setShouldReturnError(true)

        // Act
        staffListViewModel.loadInitialStaffList()

        // Assert
        val uiState = staffListViewModel.uiState.value
        Assert.assertTrue("Staff list should be empty on error", uiState.staff.isEmpty())
        Assert.assertFalse("isLoadingInitial should be false after error", uiState.isLoadingInitial)
        Assert.assertNotNull("Error should not be null on failure", uiState.error)
        Assert.assertEquals("Test Exception: Failed to fetch staff list", uiState.error)
    }

    @Test
    fun `loadMoreStaff should append new staff list to existing list`() = runTest {
        // Arrange: Load the first page
        staffListViewModel.loadInitialStaffList()
        val initialStaffCount = staffListViewModel.uiState.value.staff.size
        Assert.assertEquals("Initial load should have 2 staff members", 2, initialStaffCount)

        // Act: Load the next page
        staffListViewModel.loadMoreStaff()

        // Assert
        val uiState = staffListViewModel.uiState.value
        Assert.assertEquals(
            "Total staff count should be 4 after loading more",
            4,
            uiState.staff.size
        )
        Assert.assertFalse("isLoadingMore should be false after loading", uiState.isLoadingMore)
        Assert.assertNull("Error should be null on success", uiState.error)
        Assert.assertTrue("isLastPage should be true after loading page 2 of 2", uiState.isLastPage)
    }

    @Test
    fun `loadMoreStaff should do nothing if it is already the last page`() = runTest {
        // Arrange: Load the first and second pages, until the end
        staffListViewModel.loadInitialStaffList()
        staffListViewModel.loadMoreStaff()
        val staffCountAfterLoadingAll = staffListViewModel.uiState.value.staff.size
        Assert.assertTrue("isLastPage should be true", staffListViewModel.uiState.value.isLastPage)

        // Act: Try to load more again
        staffListViewModel.loadMoreStaff()

        // Assert
        val uiState = staffListViewModel.uiState.value
        // The list count should not increase
        Assert.assertEquals(
            "Staff count should remain unchanged",
            staffCountAfterLoadingAll,
            uiState.staff.size
        )
        Assert.assertFalse("isLoadingMore should remain false", uiState.isLoadingMore)
    }

    @Test
    fun `setLoginToken should update the token in uiState`() = runTest {
        // Arrange
        val testToken = "test-token-123"

        // Act
        staffListViewModel.setLoginToken(testToken)

        // Assert
        // Use .first() to get the current value of StateFlow
        val currentToken = staffListViewModel.uiState.first().loginToken
        Assert.assertEquals("Token in uiState should be updated", testToken, currentToken)
    }
}