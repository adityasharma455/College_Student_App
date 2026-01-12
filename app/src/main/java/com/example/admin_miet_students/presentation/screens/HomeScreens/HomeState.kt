package com.example.admin_miet_students.presentation.screens.HomeScreens


import com.example.admin_miet_students.domain.models.LocationDataModel
import com.example.admin_miet_students.domain.models.StudentDataModel

sealed class HomeState {
    object Loading : HomeState()
    data class Error(val message: String) : HomeState()

    data class Success(
        val student: StudentDataModel,
        val location: LocationDataModel?
    ) : HomeState()
}
