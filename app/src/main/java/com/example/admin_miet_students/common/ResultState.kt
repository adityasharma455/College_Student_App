package com.example.admin_miet_students.common

sealed class ResultState<out T> {
    data class Success<out T>(var data: @UnsafeVariance T): ResultState<T>()
    data class Error<out T>(var message: String): ResultState<T>()
    object Loading: ResultState<Nothing>()
}