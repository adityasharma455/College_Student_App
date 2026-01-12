package com.example.admin_miet_students.domain.repo

import com.example.admin_miet_students.common.ResultState
import com.example.admin_miet_students.domain.models.StudentDataModel
import kotlinx.coroutines.flow.Flow

interface AuthRepo {
    fun registerStudent(studentDataModel: StudentDataModel) : Flow<ResultState<Boolean>>
    fun signInStudent(email: String, password: String): Flow<ResultState<StudentDataModel>>
    fun getStudent(): Flow<ResultState<StudentDataModel>>
    fun signOutStudent(): Flow<ResultState<Boolean>>
    fun updateStudent(studentDataModel: StudentDataModel): Flow<ResultState<Boolean>>
}