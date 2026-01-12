package com.example.admin_miet_students.presentation.screens.AuthScreens.RegisterScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.admin_miet_students.common.ResultState
import com.example.admin_miet_students.domain.models.StudentDataModel
import com.example.admin_miet_students.domain.useCases.AuthUseCase
import com.example.admin_miet_students.presentation.screens.AuthScreens.AuthScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SignUpViewModel(
    private val authUseCase: AuthUseCase
): ViewModel() {

    private val _signUpState = MutableStateFlow<AuthScreenState>(AuthScreenState.Idle)
    val signUpState = _signUpState.asStateFlow()

    fun registerStudent(studentDataModel: StudentDataModel){
        viewModelScope.launch(Dispatchers.IO) {
            authUseCase.registerStudent(studentDataModel).collectLatest { result->
                when(result){
                    is ResultState.Loading -> {
                        _signUpState.value = AuthScreenState.Loading
                    }
                    is ResultState.Success<Boolean> -> {
                        if (result.data == true){
                            _signUpState.value = AuthScreenState.RegistrationSuccess(Success = result.data)
                        }
                        else {
                            _signUpState.value = AuthScreenState.Error("Registration Returend False")
                        }
                    }
                    is ResultState.Error -> {
                        _signUpState.value = AuthScreenState.Error(result.message.toString() ?: "Registration Failed")
                    }
                }


            }

        }
    }



}