## Core Features
* User Login: A secure login screen with input validation and asynchronous API handling.
* Staff Directory: Displays a list of staff members after a successful login.
* Implements a 'Load More' feature to fetch subsequent pages of staff data.

## Tech Stack & Tools
* Language: Kotlin
* Architecture: MVVM (Model-View-ViewModel)
* UI Framework: Android's traditional View System (XML Layouts)

## Jetpack Components:

* ViewModel: Manages UI-related data and business logic.

* LiveData / StateFlow: Used for reactive programming, notifying the UI of data changes.

* Data Binding: Binds UI components in layouts to data sources, reducing boilerplate code.

* Navigation Component: Manages in-app navigation between fragments.

* Asynchronous Programming: Kotlin Coroutines: Handles asynchronous tasks like network requests.

* Dependency Injection: Manual Dependency Injection.

* Unit Testing: JUnit 4, kotlinx-coroutines-test

## Project Structure
```
com.example.staffmanagementapp/
├── data                    # Data layer, responsible for all data handling
│   ├── api                 # Handles network requests
│   │   ├── AuthApiService.kt
│   │   ├── BaseApiService.kt
│   │   ├── DefaultAuthApiService.kt
│   │   ├── DefaultStaffApiService.kt
│   │   ├── FakeAuthApiService.kt
│   │   ├── FakeStaffApiService.kt
│   │   └── StaffApiService.kt
│   │
│   ├── model               # Data models
│   │   ├── domain          # Business models used within the app (Domain)
│   │   │   ├── Staff.kt
│   │   │   ├── StaffListPage.kt
│   │   │   └── UserToken.kt
│   │   └── dto             # Data Transfer Objects, matching the API JSON structure
│   │       ├── LoginRequest.kt
│   │       ├── LoginResponse.kt
│   │       └── StaffListResponse.kt
│   │
│   └── repository          # Repository layer, the single source of truth
│       ├── AuthRepository.kt
│       ├── AuthRepositoryImpl.kt
│       ├── StaffRepository.kt
│       └── StaffRepositoryImpl.kt
│
├── di                      # Dependency Injection
│   └── Injection.kt        # Central factory for manual dependency injection
│
├── ui                      # UI layer, contains all screen-related components
│   ├── login
│   │   ├── LoginFragment.kt
│   │   ├── LoginState.kt
│   │   └── LoginViewModel.kt
│   │
│   ├── stafflist
│   │   ├── StaffDiffCallback.kt
│   │   ├── StaffListAdapter.kt
│   │   ├── StaffListFragment.kt
│   │   ├── StaffListUiState.kt
│   │   └── StaffListViewModel.kt
│   │
│   └── MainActivity.kt
│
└── util                    # Common utility classes
    ├── ImageLoader.kt
    └── Result.kt
```
