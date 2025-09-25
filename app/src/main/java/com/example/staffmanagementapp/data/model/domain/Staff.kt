
package com.example.staffmanagementapp.data.model.domain

/**
 * Represents a single staff member in the domain model.
 * This is the staff object used by the app in the UI layer and business logic layer.
 * It is converted from the StaffDto in the data layer.
 *
 * The advantage of this design is that even if the API's DTO format changes in the future,
 * as long as the conversion logic is updated, the UI and ViewModel will not be affected.
 *
 * @property id The unique ID of the staff member.
 * @property email The email address of the staff member.
 * @property fullName The full name of the staff member (concatenated from firstName and lastName).
 * @property avatarUrl The URL of the staff member's avatar image.
 */
data class Staff(
    val id: Int,
    val email: String,
    val fullName: String,

    // Property name changed from avatar to avatarUrl to more clearly express that it is a URL
    val avatarUrl: String
)
