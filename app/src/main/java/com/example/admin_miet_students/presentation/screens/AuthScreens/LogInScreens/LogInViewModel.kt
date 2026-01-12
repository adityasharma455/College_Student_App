package com.example.admin_miet_students.presentation.screens.AuthScreens.LogInScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.admin_miet_students.common.ResultState
import com.example.admin_miet_students.domain.models.StudentDataModel
import com.example.admin_miet_students.domain.useCases.AuthUseCase
import com.example.admin_miet_students.presentation.screens.AuthScreens.AuthScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class LogInViewModel(
    private val authUseCase: AuthUseCase
): ViewModel() {

    private val _logInState= MutableStateFlow<AuthScreenState>(AuthScreenState.Idle)
    val logInState = _logInState.asStateFlow()

    fun logInStudent(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            authUseCase.signInStudent(email, password).collectLatest { result ->
                when(result){
                    is ResultState.Loading -> {
                        _logInState.value = AuthScreenState.Loading
                    }
                    is ResultState.Success<StudentDataModel> ->{
                        _logInState.value = AuthScreenState.LogInSuccess(Success = result.data)

                    }
                    is ResultState.Error ->{
                        _logInState.value = AuthScreenState.Error(result.message.toString() ?: "Failed to LogIn")
                    }
                }

            }
        }
    }

    // 🔹 Sign out current student
    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _logInState.value = AuthScreenState.Loading
                authUseCase.signOutStudent()  // ✅ calling your use case
                _logInState.value = AuthScreenState.signOut
            } catch (e: Exception) {
                _logInState.value = AuthScreenState.Error(e.message ?: "Sign out failed")
            }
        }
    }
}