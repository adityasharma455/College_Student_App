package com.example.admin_miet_students.presentation.screens.HomeScreens



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.admin_miet_students.common.ResultState
import com.example.admin_miet_students.domain.models.StudentDataModel
import com.example.admin_miet_students.domain.useCases.AuthUseCase
import com.example.admin_miet_students.domain.useCases.BusUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authUseCase: AuthUseCase,
    private val busUseCase: BusUseCase
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState = _homeState.asStateFlow()

    fun loadHomeScreen() {
        viewModelScope.launch(Dispatchers.IO) {

            // STEP 1: Get Student Profile
            authUseCase.getStudent().collectLatest { studentResult ->
                when (studentResult) {

                    is ResultState.Loading -> _homeState.value = HomeState.Loading

                    is ResultState.Error ->
                        _homeState.value = HomeState.Error(studentResult.message)

                    is ResultState.Success -> {
                        val student = studentResult.data

                        // STEP 2: Check if bus exists
                        busUseCase.checkBusExists(student.busId)
                            .collectLatest { busExistResult ->

                                when (busExistResult) {

                                    is ResultState.Error ->
                                        _homeState.value = HomeState.Error(busExistResult.message)

                                    is ResultState.Success -> {
                                        if (!busExistResult.data) {
                                            _homeState.value = HomeState.Error("Bus ID does not exist")
                                            return@collectLatest
                                        }

                                        // STEP 3: Listen to bus location
                                        listenToBusLocation(student.busId, student)
                                    }

                                    else -> Unit
                                }
                            }
                    }
                }
            }
        }
    }

    private fun listenToBusLocation(busId: String, student: StudentDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            busUseCase.listenToBusLocation(busId).collectLatest { locResult ->

                when (locResult) {
                    is ResultState.Loading -> {
                        _homeState.value = HomeState.Loading
                    }

                    is ResultState.Error -> {
                        _homeState.value = HomeState.Error(locResult.message)
                    }

                    is ResultState.Success -> {
                        _homeState.value = HomeState.Success(
                            student = student,
                            location = locResult.data
                        )
                    }
                }
            }
        }
    }
}
