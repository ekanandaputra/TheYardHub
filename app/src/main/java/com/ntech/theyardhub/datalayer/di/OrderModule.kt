package com.ntech.theyardhub.datalayer.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.implementation.repository.OrderRepositoryImpl
import com.ntech.theyardhub.datalayer.repository.OrderRepository

object OrderModule {
    fun provideOrderRef(): CollectionReference {
        return Firebase.firestore.collection("orders")
    }

    fun provideOrderRepository(
        orderRef: CollectionReference,
        dataStorage: DataStorage,
    ): OrderRepository {
        return OrderRepositoryImpl(orderRef, dataStorage)
    }
}
