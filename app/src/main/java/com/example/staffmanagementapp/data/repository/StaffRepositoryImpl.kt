package com.example.staffmanagementapp.data.repository

import com.example.staffmanagementapp.data.api.StaffApiService
import com.example.staffmanagementapp.data.model.domain.Staff
import com.example.staffmanagementapp.data.model.domain.StaffListPage
import com.example.staffmanagementapp.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @param staffApiService The service responsible for handling network requests.
 */
class StaffRepositoryImpl(
    private val staffApiService: StaffApiService
) : StaffRepository {

    override suspend fun getStaffList(page: Int): Result<StaffListPage> {
        return withContext(Dispatchers.IO) {
            try {
                val response = staffApiService.getStaffList(page)

                // Convert DTO to Domain Model
                val staffList = response.data.map { staffDto ->
                    Staff(
                        id = staffDto.id,
                        email = staffDto.email,
                        fullName= "${staffDto.firstName} ${staffDto.lastName}",
                        avatarUrl = staffDto.avatar
                    )
                }

                val staffListPage = StaffListPage(
                    staff = staffList,
                    currentPage = response.page,
                    totalPages = response.totalPages
                )

                // Return a successful Result
                Result.Success(staffListPage)
            } catch (e: Exception) {
                // Catch the exception and return a failed Result
                Result.Error(e)
            }
        }
    }
}

