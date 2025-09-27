package com.example.staffmanagementapp.data.repository

import com.example.staffmanagementapp.data.model.domain.Staff
import com.example.staffmanagementapp.data.model.domain.StaffListPage
import com.example.staffmanagementapp.util.Result

/**
 * A fake implementation of StaffRepository for unit testing.
 * It simulates paginated data loading and error behavior.
 */
class FakeStaffRepository : StaffRepository {

    private var shouldReturnError = false
    private val fakeData = mutableMapOf<Int, StaffListPage>()

    init {
        // Prepare fake data for first and second pages
        fakeData[1] = StaffListPage(
            staff = listOf(
                Staff(1, "george.bluth@reqres.in", "George Bluth", "https://reqres.in/img/faces/1-image.jpg"),
                Staff(2, "janet.weaver@reqres.in", "Janet Weaver", "https://reqres.in/img/faces/2-image.jpg")
            ),
            currentPage = 1,
            totalPages = 2
        )
        fakeData[2] = StaffListPage(
            staff = listOf(
                Staff(3, "emma.wong@reqres.in", "Emma Wong", "https://reqres.in/img/faces/3-image.jpg"),
                Staff(4, "eve.holt@reqres.in", "Eve Holt", "https://reqres.in/img/faces/4-image.jpg")
            ),
            currentPage = 2,
            totalPages = 2
        )
    }

    /**
     * Used in tests to control whether this Fake Repository should return errors.
     */
    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getStaffList(page: Int): Result<StaffListPage> {
        if (shouldReturnError) {
            return Result.Error(Exception("Test Exception: Failed to fetch staff list"))
        }

        // Simulate API behavior: if the requested page has fake data, return it; otherwise return an empty success result.
        return fakeData[page]?.let {
            Result.Success(it)
        } ?: Result.Success(StaffListPage(staff = emptyList(), currentPage = page, totalPages = 2))
    }
}
