package com.example.staffmanagementapp.ui.stafflist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.staffmanagementapp.data.model.domain.Staff
import com.example.staffmanagementapp.databinding.ItemStaffBinding
import com.example.staffmanagementapp.util.ImageLoader

class StaffListAdapter : ListAdapter<Staff, StaffListAdapter.StaffViewHolder>(StaffDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        val binding = ItemStaffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StaffViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        val staffItem = getItem(position)
        holder.bind(staffItem)
    }

    class StaffViewHolder(private val binding: ItemStaffBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(staff: Staff) {
            binding.staff = staff
            // Asynchronously load avatar
            ImageLoader.load(binding.imageViewAvatar, staff.avatarUrl)
            binding.executePendingBindings()
        }
    }
}
