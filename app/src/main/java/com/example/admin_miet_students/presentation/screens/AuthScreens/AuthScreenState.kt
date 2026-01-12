package com.example.admin_miet_students.presentation.screens.AuthScreens

import com.example.admin_miet_students.domain.models.StudentDataModel

sealed class AuthScreenState {

    object  Idle: AuthScreenState()
    object Loading: AuthScreenState()
    data class RegistrationSuccess(val Success: Boolean) : AuthScreenState()
    data class LogInSuccess(val Success: StudentDataModel): AuthScreenState()

    object signOut: AuthScreenState()

    data class Error(val message: String): AuthScreenState()
}