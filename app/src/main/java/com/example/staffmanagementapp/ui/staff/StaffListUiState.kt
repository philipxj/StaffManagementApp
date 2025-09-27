package com.example.staffmanagementapp.ui.stafflist

import com.example.staffmanagementapp.data.model.domain.Staff

/**
 * Represents all UI states for the staff list screen.
 * This is a single data class that allows the UI layer to simply observe one object to get all the necessary information.
 *
 * @param isLoadingInitial Whether the first page of data is being loaded (displays full-screen loading).
 * @param isLoadingMore Whether the next page of data is being loaded (displays loading at the bottom of the list).
 * @param staff The currently loaded list of staff.
 * @param error Error message, if null then there is no error.
 * @param isLastPage Whether the last page has been reached, used to determine if the "Load More" button still needs to be displayed.
 * @param loginToken The user token passed from the login page, needs to be displayed on the screen.
 */
data class StaffListUiState(
    val isLoadingInitial: Boolean = false,
    val isLoadingMore: Boolean = false,
    val staff: List<Staff> = emptyList(),
    val error: String? = null,
    val isLastPage: Boolean = false,
    val loginToken: String = ""
)
