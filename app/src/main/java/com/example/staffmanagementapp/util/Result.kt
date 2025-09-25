package com.example.staffmanagementapp.util

/**
 * A generic wrapper class for representing the result of an asynchronous operation.
 * It can be either a success [Success] or an error [Error].
 *
 * @param T The type of data carried in case of success.
 */
sealed class Result<out T : Any> {

    /**
     * Represents the successful state of an operation.
     * @property data The data returned on success.
     */
    data class Success<out T : Any>(val data: T) : Result<T>()

    /**
     * Represents the failed state of an operation.
     * @property exception The exception caught on failure.
     */
    data class Error(val exception: Exception) : Result<Nothing>()
}
