package com.example.staffmanagementapp.data.model.domain

/**
 * A domain model for a staff list page.
 * Contains the list of staff data and pagination status.
 *
 * @param staff The list of staff for the current page.
 * @param currentPage The current page number.
 * @param totalPages The total number of pages.
 */
data class StaffListPage(
    val staff: List<Staff>,
    val currentPage: Int,
    val totalPages: Int
) {
    /**
     * Calculates if there is a next page.
     */
    val hasNextPage: Boolean
        get() = currentPage < totalPages
}
