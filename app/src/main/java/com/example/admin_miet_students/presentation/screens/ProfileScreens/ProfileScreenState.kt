package com.example.admin_miet_students.presentation.screens.ProfileScreens

import com.example.admin_miet_students.domain.models.StudentDataModel

sealed class ProfileScreenState {
    object Loading : ProfileScreenState()
    data class Success(val studentDataModel: StudentDataModel) : ProfileScreenState()
    data class Error(val message: String) : ProfileScreenState()
}
