package com.example.staffmanagementapp.data.api

import com.example.staffmanagementapp.data.model.dto.StaffListResponse

/**
 * Get Staff data.
 */
interface StaffApiService {

    /**
     * Retrieves the staff list for a given page number.
     *
     * @param page The page number to request.
     * @return A response object containing the staff list and pagination information.
     * @throws Exception If the network request fails or the API returns an error.
     */
    suspend fun getStaffList(page: Int): StaffListResponse
}
