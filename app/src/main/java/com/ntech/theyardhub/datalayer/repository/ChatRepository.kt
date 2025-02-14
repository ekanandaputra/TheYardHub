package com.ntech.theyardhub.datalayer.repository

import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.ChatListModel
import com.ntech.theyardhub.datalayer.model.ChatMessageModel
import com.ntech.theyardhub.datalayer.model.ChatRoomModel


interface ChatRepository {

    suspend fun getChatMessage(chatRoomId: String): AppResponse<List<ChatMessageModel>>

    suspend fun sendMessage(message: String, chatRoomId: String): AppResponse<ChatMessageModel>

    suspend fun createChatRoom(request: ChatRoomModel): AppResponse<ChatRoomModel>

    suspend fun getChatRoomByParticipantId(participants: List<String>): AppResponse<ChatRoomModel>

    suspend fun getChatRooms(): AppResponse<List<ChatListModel>>

}