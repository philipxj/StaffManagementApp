package com.example.staffmanagementapp.ui.stafflist

import androidx.recyclerview.widget.DiffUtil
import com.example.staffmanagementapp.data.model.domain.Staff

/**
 * Callback for DiffUtil to help RecyclerView update the list efficiently.
 */
object StaffDiffCallback : DiffUtil.ItemCallback<Staff>() {
    override fun areItemsTheSame(oldItem: Staff, newItem: Staff): Boolean {
        // ID is the unique identifier
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Staff, newItem: Staff): Boolean {
        // data class automatically implements equals(), so we can compare contents directly
        return oldItem == newItem
    }
}
