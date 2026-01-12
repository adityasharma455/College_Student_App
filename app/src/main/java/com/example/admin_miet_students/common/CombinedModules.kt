package com.example.admin_miet_students.common

import com.example.admin_miet_students.data.di.dataModule
import com.example.admin_miet_students.domain.di.domainModule
import com.example.admin_miet_students.presentation.di.presentationModule

var CombinedModules = listOf(
    dataModule,
    domainModule,
    presentationModule
)