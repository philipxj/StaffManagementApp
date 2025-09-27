package com.example.staffmanagementapp.ui.stafflist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.staffmanagementapp.data.repository.StaffRepository
import com.example.staffmanagementapp.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Responsible for handling the business logic and state management of the staff list screen.
 */
class StaffListViewModel(
    private val staffRepository: StaffRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StaffListUiState())
    val uiState: StateFlow<StaffListUiState> = _uiState.asStateFlow()

    private var currentPage = 0
    private var totalPages = 1 // Defaults to 1 to avoid errors before fetching real data

    /**
     * Sets the user token passed from the login page.
     */
    fun setLoginToken(token: String) {
        _uiState.update { it.copy(loginToken = token) }
    }

    /**
     * Loads the first page of staff data.
     */
    fun loadInitialStaffList() {
        // If it's already loading or data is already present, don't reload
        if (_uiState.value.isLoadingInitial || _uiState.value.staff.isNotEmpty()) {
            return
        }
        fetchStaffList(page = 1, isInitialLoad = true)
    }

    /**
     * Loads the next page of staff data.
     */
    fun loadMoreStaff() {
        val currentState = _uiState.value
        // If it's already loading or it's the last page, don't load
        if (currentState.isLoadingMore || currentState.isLastPage) {
            return
        }
        fetchStaffList(page = currentPage + 1, isInitialLoad = false)
    }

    private fun fetchStaffList(page: Int, isInitialLoad: Boolean) {
        viewModelScope.launch {
            // Update the corresponding loading state based on whether it's an initial load
            _uiState.update {
                it.copy(
                    isLoadingInitial = isInitialLoad,
                    isLoadingMore = !isInitialLoad,
                    error = null
                )
            }

            when (val result = staffRepository.getStaffList(page)) {
                is Result.Success -> {
                    val staffListPage = result.data
                    totalPages = staffListPage.totalPages
                    currentPage = staffListPage.currentPage

                    _uiState.update { currentState ->
                        val currentList = if (isInitialLoad) emptyList() else currentState.staff
                        val newList = currentList + staffListPage.staff
                        currentState.copy(
                            isLoadingInitial = false,
                            isLoadingMore = false,
                            staff = newList,
                            isLastPage = currentPage >= totalPages
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingInitial = false,
                            isLoadingMore = false,
                            error = result.exception.message ?: "An unknown error occurred"
                        )
                    }
                }
            }
        }
    }
}
