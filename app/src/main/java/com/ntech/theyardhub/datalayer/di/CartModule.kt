package com.ntech.theyardhub.datalayer.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ntech.theyardhub.datalayer.implementation.repository.CartRepositoryImpl
import com.ntech.theyardhub.datalayer.repository.CartRepository

object CartModule {
    fun provideCartRef(): CollectionReference {
        return Firebase.firestore.collection("cart")
    }

    fun provideCartRepository(
        cartRef: CollectionReference
    ): CartRepository {
        return CartRepositoryImpl(cartRef)
    }
}
