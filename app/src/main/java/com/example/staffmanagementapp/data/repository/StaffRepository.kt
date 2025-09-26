package com.example.staffmanagementapp.data.repository

import com.example.staffmanagementapp.data.model.domain.StaffListPage
import  com.example.staffmanagementapp.util.Result

/**
 * Data operations interface related to the staff list.
 */
interface StaffRepository {

    /**
     * Retrieves the staff list for the specified page number.
     *
     * @param page The page number to request.
     * @return A Result object, containing StaffListPage on success or an exception on failure.
     */
    suspend fun getStaffList(page: Int): Result<StaffListPage>
}
