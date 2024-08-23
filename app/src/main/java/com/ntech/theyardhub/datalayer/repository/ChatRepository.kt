package com.ntech.theyardhub.datalayer.repository

import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.ChatMessageModel


interface ChatRepository {

    suspend fun getChatMessage(): AppResponse<List<ChatMessageModel>>

    suspend fun sendMessage(message: String): AppResponse<ChatMessageModel>

}