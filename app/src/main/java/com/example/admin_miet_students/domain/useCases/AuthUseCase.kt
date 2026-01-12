package com.example.admin_miet_students.domain.useCases

import com.example.admin_miet_students.common.ResultState
import com.example.admin_miet_students.domain.models.StudentDataModel
import com.example.admin_miet_students.domain.repo.AuthRepo
import kotlinx.coroutines.flow.Flow

class AuthUseCase (private val repo: AuthRepo) {

    fun registerStudent(studentDataModel: StudentDataModel): Flow<ResultState<Boolean>> {
        return repo.registerStudent(studentDataModel)
    }

    fun signInStudent(email: String, password: String): Flow<ResultState<StudentDataModel>> {
        return repo.signInStudent(email, password)
    }

    fun getStudent(): Flow<ResultState<StudentDataModel>> {
        return repo.getStudent()
    }

    fun signOutStudent(): Flow<ResultState<Boolean>> {
        return repo.signOutStudent()
    }

    fun updateStudent(studentDataModel: StudentDataModel): Flow<ResultState<Boolean>> {
        return repo.updateStudent(studentDataModel)
    }

}