package com.ntech.theyardhub.datalayer.repository

import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.ChatMessageModel
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.YardModel


interface YardRepository {

    suspend fun getFarms(): AppResponse<List<YardModel>>

    suspend fun getFarm(documentId: String): AppResponse<YardModel>

    suspend fun getFarmByUserId(): AppResponse<YardModel>

    suspend fun createFarm(request: YardModel): AppResponse<YardModel>

    suspend fun updateFarm(documentId: String, request: YardModel): AppResponse<YardModel>

}