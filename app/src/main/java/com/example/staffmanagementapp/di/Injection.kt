package com.example.staffmanagementapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.staffmanagementapp.data.api.AuthApiService
import com.example.staffmanagementapp.data.api.DefaultAuthApiService
import com.example.staffmanagementapp.data.api.DefaultStaffApiService
import com.example.staffmanagementapp.data.api.StaffApiService
import com.example.staffmanagementapp.data.repository.AuthRepository
import com.example.staffmanagementapp.data.repository.AuthRepositoryImpl
import com.example.staffmanagementapp.data.repository.StaffRepository
import com.example.staffmanagementapp.data.repository.StaffRepositoryImpl
import com.example.staffmanagementapp.ui.login.LoginViewModel
import com.example.staffmanagementapp.ui.stafflist.StaffListViewModel

/**
 * The central hub for manual dependency injection.
 * Responsible for creating and providing various dependency instances needed by the App.
 * This is an object (singleton), ensuring only one instance throughout the App.
 */
object Injection {

    // --- API Service Providers ---

    private fun provideAuthApiService(): AuthApiService {
        return DefaultAuthApiService()
    }

    private fun provideStaffApiService(): StaffApiService {
        return DefaultStaffApiService()
    }

    // --- Repository Providers ---

    private fun provideAuthRepository(): AuthRepository {
        return AuthRepositoryImpl(provideAuthApiService())
    }

    private fun provideStaffRepository(): StaffRepository {
        return StaffRepositoryImpl(provideStaffApiService())
    }

    // --- ViewModel Factory ---

    /**
     * A custom ViewModel factory.
     * It knows how to create ViewModels with parameters (dependencies).
     */
    val viewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                // If LoginViewModel is requested, create one and pass in AuthRepository
                modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                    LoginViewModel(provideAuthRepository()) as T
                }
                 modelClass.isAssignableFrom(StaffListViewModel::class.java) -> {
                     StaffListViewModel(provideStaffRepository()) as T
                 }
                else -> {
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }
        }
    }
}

