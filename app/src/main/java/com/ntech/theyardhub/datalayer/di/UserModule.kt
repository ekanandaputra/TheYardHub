package com.ntech.theyardhub.datalayer.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.implementation.repository.AuthenticationRepositoryImpl
import com.ntech.theyardhub.datalayer.implementation.repository.UserRepositoryImp
import com.ntech.theyardhub.datalayer.repository.AuthenticationRepository
import com.ntech.theyardhub.datalayer.repository.UserRepository


object UserModule {
    fun provideAuthRef(): CollectionReference {
        return Firebase.firestore.collection("users")
    }

    fun provideAuthRepository(
        authRef: CollectionReference,
        dataStorage: DataStorage,
    ): AuthenticationRepository {
        return AuthenticationRepositoryImpl(authRef, dataStorage)
    }

    fun provideUserRepository(
        userRef: CollectionReference,
        dataStorage: DataStorage,
    ): UserRepository {
        return UserRepositoryImp(userRef, dataStorage)
    }
}