package com.ntech.theyardhub.datalayer.repository

import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.RegisterRequest
import com.ntech.theyardhub.datalayer.model.UserModel


interface AuthenticationRepository {

    suspend fun postLogin(username: String, password: String): AppResponse<UserModel>?

    suspend fun getUsername(): String

    suspend fun postRegister(request: RegisterRequest): AppResponse<UserModel>?

}