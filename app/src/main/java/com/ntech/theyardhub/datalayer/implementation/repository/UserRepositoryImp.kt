package com.ntech.theyardhub.datalayer.implementation.repository

import com.google.firebase.firestore.CollectionReference
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.model.UserModel
import com.ntech.theyardhub.datalayer.repository.UserRepository

class UserRepositoryImp(
    private val userRef: CollectionReference,
    private val dataStorage: DataStorage
) : UserRepository {
    override suspend fun setUserDocumentId(userDocumentId: String) {
        dataStorage.userDocumentId = userDocumentId
    }

    override suspend fun getUserDetail(userDocumentId: String): AppResponse<UserModel>? {
        TODO("Not yet implemented")
    }

}