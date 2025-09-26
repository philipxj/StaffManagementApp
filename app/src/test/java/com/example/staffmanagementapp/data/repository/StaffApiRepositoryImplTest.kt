package com.example.staffmanagementapp.data.repository

import com.example.staffmanagementapp.data.api.StaffApiService
import com.example.staffmanagementapp.data.model.domain.StaffListPage
import com.example.staffmanagementapp.data.model.dto.StaffDto
import com.example.staffmanagementapp.data.model.dto.StaffListResponse
import com.example.staffmanagementapp.util.Result
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class StaffRepositoryImplTest {

    // System Under Test (SUT)
    private lateinit var staffRepository: StaffRepositoryImpl

    // Fake ApiService to simulate network responses
    private lateinit var fakeStaffApiService: FakeStaffApiService

    @Before
    fun setUp() {
        fakeStaffApiService = FakeStaffApiService()
        staffRepository = StaffRepositoryImpl(fakeStaffApiService)
    }

    @Test
    fun `getStaffList success, maps DTO to Domain model correctly`() = runTest {
        // Arrange: Prepare fake data and set Fake ApiService to return success
        val fakeResponse = StaffListResponse(
            page = 1,
            perPage = 2,
            total = 4,
            totalPages = 2,
            data = listOf(
                StaffDto(1, "george.bluth@reqres.in", "George", "Bluth", "avatar1.jpg"),
                StaffDto(2, "janet.weaver@reqres.in", "Janet", "Weaver", "avatar2.jpg")
            )
        )
        fakeStaffApiService.setResponse(fakeResponse)

        // Act: Execute the method under test
        val result = staffRepository.getStaffList(1)

        // Assert: Verify the result
        assertTrue(result is Result.Success)
        val staffListPage = (result as Result.Success<StaffListPage>).data

        assertEquals(1, staffListPage.currentPage)
        assertEquals(2, staffListPage.totalPages)
        assertEquals(2, staffListPage.staff.size)

        // Verify DTO to Domain Model mapping, especially fullName
        assertEquals(1, staffListPage.staff[0].id)
        assertEquals("George Bluth", staffListPage.staff[0].fullName)
        assertEquals("avatar1.jpg", staffListPage.staff[0].avatarUrl)

        assertEquals(2, staffListPage.staff[1].id)
        assertEquals("Janet Weaver", staffListPage.staff[1].fullName)
    }

    @Test
    fun `getStaffList failure, returns error result`() = runTest {
        // Arrange: Set Fake ApiService to throw an exception
        val fakeException = IOException("Network Error")
        fakeStaffApiService.setError(fakeException)

        // Act: Execute the method under test
        val result = staffRepository.getStaffList(1)

        // Assert: Verify the result
        assertTrue(result is Result.Error)
        val exception = (result as Result.Error).exception
        assertEquals(fakeException, exception)
    }
}

/**
 * A fake implementation of [StaffApiService] for unit testing.
 * It can be configured to return successful data or throw an exception.
 */
private class FakeStaffApiService : StaffApiService {
    private var response: StaffListResponse? = null
    private var error: Exception? = null

    fun setResponse(response: StaffListResponse) {
        this.response = response
        this.error = null
    }

    fun setError(error: Exception) {
        this.error = error
        this.response = null
    }

    override suspend fun getStaffList(page: Int): StaffListResponse {
        error?.let { throw it }
        return response ?: throw IllegalStateException("Response or error must be set for the test")
    }
}
