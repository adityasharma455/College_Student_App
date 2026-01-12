package com.example.admin_miet_students.domain.di

import com.example.admin_miet_students.domain.repo.BusRepo
import com.example.admin_miet_students.domain.useCases.AuthUseCase
import com.example.admin_miet_students.domain.useCases.BusUseCase
import org.koin.dsl.module

var domainModule = module {
        factory { AuthUseCase(get()) }
        factory { BusUseCase(get<BusRepo>()) }
}