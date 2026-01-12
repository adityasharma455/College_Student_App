package com.example.admin_miet_students.data.di

import com.example.admin_miet_students.data.repoImplementation.AuthRepoImplementaion
import com.example.admin_miet_students.data.repoImplementation.BusRepoImplementation
import com.example.admin_miet_students.domain.repo.AuthRepo
import com.example.admin_miet_students.domain.repo.BusRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

var dataModule = module {
    single<FirebaseFirestore> { FirebaseFirestore.getInstance() }
    single<FirebaseAuth> { FirebaseAuth.getInstance() }

    single<BusRepoImplementation> { BusRepoImplementation(
        firestore = get()
    ) }

    single<AuthRepo> { AuthRepoImplementaion(
        firebaseAuth = get(),
        firestore = get()
    ) }

    single<BusRepo> { BusRepoImplementation(
        firestore = get()
    ) }
}