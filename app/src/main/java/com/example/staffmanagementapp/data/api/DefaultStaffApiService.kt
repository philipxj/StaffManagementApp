package com.example.staffmanagementapp.data.api

import com.example.staffmanagementapp.data.model.dto.StaffDto
import com.example.staffmanagementapp.data.model.dto.StaffListResponse
import org.json.JSONObject

/**
 * Default implementation of StaffApiService using reqres.in API
 * Handles staff list retrieval with pagination support
 */
class DefaultStaffApiService(
    httpClientProvider: HttpClientProvider = DefaultHttpClientProvider(),
    baseUrl: String = "https://reqres.in/api"
) : BaseApiService(httpClientProvider, baseUrl), StaffApiService {

    override suspend fun getStaffList(page: Int): StaffListResponse {
        return try {
            val response = executeRequest("/users?page=$page", GET)
            parseStaffListResponse(response)
        } catch (e: Exception) {
            throw RuntimeException("Failed to get staff list: ${e.message}", e)
        }
    }

    private fun parseStaffListResponse(jsonString: String): StaffListResponse {
        try {
            val jsonObject = JSONObject(jsonString)

            val page = jsonObject.getInt("page")
            val perPage = jsonObject.getInt("per_page")
            val total = jsonObject.getInt("total")
            val totalPages = jsonObject.getInt("total_pages")

            val dataArray = jsonObject.getJSONArray("data")
            val staffList = mutableListOf<StaffDto>()

            for (i in 0 until dataArray.length()) {
                val staffJson = dataArray.getJSONObject(i)
                val staff = StaffDto(
                    id = staffJson.getInt("id"),
                    email = staffJson.getString("email"),
                    firstName = staffJson.getString("first_name"),
                    lastName = staffJson.getString("last_name"),
                    avatar = staffJson.getString("avatar")
                )
                staffList.add(staff)
            }

            return StaffListResponse(
                page = page,
                perPage = perPage,
                total = total,
                totalPages = totalPages,
                data = staffList
            )

        } catch (e: Exception) {
            throw RuntimeException("Failed to parse staff list response: ${e.message}", e)
        }
    }
}