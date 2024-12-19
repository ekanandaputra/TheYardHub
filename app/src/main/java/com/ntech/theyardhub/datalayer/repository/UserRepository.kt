package com.ntech.theyardhub.datalayer.repository

import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.RegisterRequest
import com.ntech.theyardhub.datalayer.model.UserModel
import com.ntech.theyardhub.datalayer.model.YardModel


interface UserRepository {

    suspend fun setUserDocumentId(userDocumentId: String)

    suspend fun getUserDetail(): AppResponse<UserModel>?

    suspend fun updateYard(request: YardModel): AppResponse<YardModel>

}