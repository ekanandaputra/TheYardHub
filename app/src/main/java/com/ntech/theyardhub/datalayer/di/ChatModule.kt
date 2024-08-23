package com.ntech.theyardhub.datalayer.di

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.implementation.repository.AuthenticationRepositoryImpl
import com.ntech.theyardhub.datalayer.implementation.repository.ChatRepositoryImpl
import com.ntech.theyardhub.datalayer.repository.AuthenticationRepository
import com.ntech.theyardhub.datalayer.repository.ChatRepository


object ChatModule {
    fun provideChatRef(): CollectionReference {
        return Firebase.firestore.collection("chat")
    }

    fun provideChatRepository(
        chatRef: CollectionReference,
        dataStorage: DataStorage,
    ): ChatRepository {
        return ChatRepositoryImpl(chatRef, dataStorage)
    }
}