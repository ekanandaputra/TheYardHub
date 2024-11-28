package com.ntech.theyardhub.datalayer.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.implementation.repository.AuthenticationRepositoryImpl
import com.ntech.theyardhub.datalayer.implementation.repository.YardRepositoryImp
import com.ntech.theyardhub.datalayer.repository.AuthenticationRepository
import com.ntech.theyardhub.datalayer.repository.YardRepository


object YardModule {
    fun provideYardRef(): CollectionReference {
        return Firebase.firestore.collection("farms")
    }

    fun provideYardRepository(
        yardRef: CollectionReference,
        dataStorage: DataStorage,
    ): YardRepository {
        return YardRepositoryImp(yardRef, dataStorage)
    }
}