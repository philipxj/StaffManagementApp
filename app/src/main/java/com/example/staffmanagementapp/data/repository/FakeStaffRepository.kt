package com.example.staffmanagementapp.data.repository

import com.example.staffmanagementapp.data.model.domain.Staff
import com.example.staffmanagementapp.data.model.domain.StaffListPage
import com.example.staffmanagementapp.util.Result

/**
 * StaffRepository 的一個假實現，專門用於單元測試。
 * 它模擬了分頁加載數據和返回錯誤的行為。
 */
class FakeStaffRepository : StaffRepository {

    private var shouldReturnError = false
    private val fakeData = mutableMapOf<Int, StaffListPage>()

    init {
        // 預先準備好第一頁和第二頁的假數據
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
     * 在測試中用來控制這個 Fake Repository 是否應該返回錯誤。
     */
    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getStaffList(page: Int): Result<StaffListPage> {
        if (shouldReturnError) {
            return Result.Error(Exception("Test Exception: Failed to fetch staff list"))
        }

        // 模擬 API 行為：如果請求的頁數存在假數據，就返回數據；否則返回一個空的成功結果。
        return fakeData[page]?.let {
            Result.Success(it)
        } ?: Result.Success(StaffListPage(staff = emptyList(), currentPage = page, totalPages = 2))
    }
}
