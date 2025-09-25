package com.example.staffmanagementapp.data.model.dto

/**
 * Top-level Data Transfer Object (DTO) for receiving the staff list API response.
 * This class includes pagination information and a list of staff data.
 *
 * In a real-world project, we would use annotations like @SerializedName("per_page")
 * to automatically map Kotlin property names (camelCase) to JSON field names (snake_case).
 * However, due to the requirement of not using third-party libraries for this exercise,
 * we will handle this mapping manually during parsing.
 *
 * @property page The current page number.
 * @property perPage The number of items displayed per page (original JSON key: per_page).
 * @property total The total number of items.
 * @property totalPages The total number of pages (original JSON key: total_pages).
 * @property data A list containing staff information.
 */
data class StaffListResponse(
    val page: Int,
    val perPage: Int,
    val total: Int,
    val totalPages: Int,
    val data: List<StaffDto>
)

/**
 * Data Transfer Object (DTO) representing individual staff information,
 * nested within [StaffListResponse].
 *
 * @property id The unique ID of the staff member.
 * @property email The email address of the staff member.
 * @property firstName The first name of the staff member (original JSON key: first_name).
 * @property lastName The last name of the staff member (original JSON key: last_name).
 * @property avatar The URL of the staff member's avatar image.
 */
data class StaffDto(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val avatar: String
)

