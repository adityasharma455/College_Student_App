package com.example.admin_miet_students.presentation.di

import com.example.admin_miet_students.presentation.screens.AuthScreens.LogInScreens.LogInViewModel
import com.example.admin_miet_students.presentation.screens.AuthScreens.RegisterScreens.SignUpViewModel
import com.example.admin_miet_students.presentation.screens.HomeScreens.HomeViewModel
import com.example.admin_miet_students.presentation.screens.ProfileScreens.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

var presentationModule = module {
    viewModel { SignUpViewModel(get()) }
    viewModel { LogInViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
}