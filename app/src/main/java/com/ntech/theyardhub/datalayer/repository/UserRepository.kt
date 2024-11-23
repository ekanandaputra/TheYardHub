package com.ntech.theyardhub.datalayer.repository

import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.RegisterRequest
import com.ntech.theyardhub.datalayer.model.UserModel


interface UserRepository {

    suspend fun setUserDocumentId(userDocumentId: String)

    suspend fun getUserDetail(userDocumentId: String): AppResponse<UserModel>?

}