package com.ntech.theyardhub.datalayer.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.implementation.repository.AuthenticationRepositoryImpl
import com.ntech.theyardhub.datalayer.implementation.repository.ChatRepositoryImpl
import com.ntech.theyardhub.datalayer.implementation.repository.PostRepositoryImpl
import com.ntech.theyardhub.datalayer.implementation.repository.ProductRepositoryImpl
import com.ntech.theyardhub.datalayer.repository.AuthenticationRepository
import com.ntech.theyardhub.datalayer.repository.ChatRepository
import com.ntech.theyardhub.datalayer.repository.PostRepository
import com.ntech.theyardhub.datalayer.repository.ProductRepository


object ProductModule {
    fun provideProductRef(): CollectionReference {
        return Firebase.firestore.collection("products")
    }

    fun provideProductRepository(
        postRef: CollectionReference,
        userRef: CollectionReference,
        dataStorage: DataStorage,
    ): ProductRepository {
        return ProductRepositoryImpl(postRef, userRef, dataStorage)
    }
}