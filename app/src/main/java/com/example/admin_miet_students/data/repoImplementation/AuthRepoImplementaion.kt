package com.example.admin_miet_students.data.repoImplementation

import com.example.admin_miet_students.common.ResultState
import com.example.admin_miet_students.common.STUDENTS_PATH
import com.example.admin_miet_students.domain.models.StudentDataModel
import com.example.admin_miet_students.domain.repo.AuthRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepoImplementaion(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): AuthRepo {

    private val studentCollection = firestore.collection(STUDENTS_PATH)

    override fun registerStudent(studentDataModel: StudentDataModel): Flow<ResultState<Boolean>> = flow{
        try {
            emit(ResultState.Loading)

            val result = firebaseAuth.createUserWithEmailAndPassword(studentDataModel.email, studentDataModel.password).await()
            val uid = result.user?.uid ?: throw Exception("Registration Failed")
            val studentData = studentDataModel.copy(uid=uid)
            studentCollection.document(uid).set(studentData).await()
            emit(ResultState.Success(true))

        }catch (e: Exception){
            emit(ResultState.Error(e.message.toString()))
        }

    }

    override fun signInStudent(
        email: String,
        password: String
    ): Flow<ResultState<StudentDataModel>> = flow {
        try {
            emit(ResultState.Loading)

            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("Login failed")

            val snapshot = studentCollection.document(uid).get().await()
            val student = snapshot.toObject(StudentDataModel::class.java)
                ?: throw Exception("Driver profile not found")

            emit(ResultState.Success(student))

        }
        catch (e: Exception){
            emit(ResultState.Error(e.message.toString()))
        }

    }

    override fun getStudent(): Flow<ResultState<StudentDataModel>> =flow{
        try {
            emit(ResultState.Loading)
            val uid = firebaseAuth.currentUser?.uid ?: throw Exception("No user logged in")
            val snapshot =studentCollection.document(uid).get().await()
            val student = snapshot.toObject(StudentDataModel::class.java)
                ?: throw Exception("Student profile not found")
            emit(ResultState.Success(student))

        }catch (e: Exception){
            emit(ResultState.Error(e.message.toString()))
        }

    }

    override fun signOutStudent(): Flow<ResultState<Boolean>> = flow {
        try {
            emit(ResultState.Loading)

            firebaseAuth.signOut()

            // Verify that user is actually signed out
            val currentUser = firebaseAuth.currentUser
            if (currentUser == null) {
                emit(ResultState.Success(true))
            } else {
                // This shouldn't happen with Firebase, but just in case
                throw Exception("Sign out failed - user still logged in")
            }

        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    override fun updateStudent(studentDataModel: StudentDataModel): Flow<ResultState<Boolean>> = flow {
        try {
            emit(ResultState.Loading)

            // Get current user's UID
            val uid = firebaseAuth.currentUser?.uid ?: throw Exception("No user logged in")

            // Verify the student being updated belongs to current user
            if (studentDataModel.uid != uid) {
                throw Exception("Cannot update another user's profile")
            }

            // Update in Firestore
            studentCollection.document(uid).set(studentDataModel).await()

            emit(ResultState.Success(true))

        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

}