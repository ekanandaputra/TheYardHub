package com.ntech.theyardhub.datalayer.repository

import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.ChatListModel
import com.ntech.theyardhub.datalayer.model.ChatMessageModel
import com.ntech.theyardhub.datalayer.model.ChatRoomModel
import com.ntech.theyardhub.datalayer.model.GroupChatRoomModel


interface GroupChatRepository {

    suspend fun getGroupChatRooms(): AppResponse<List<GroupChatRoomModel>>

    suspend fun getMessages(groupChatRoomId: String): AppResponse<List<ChatMessageModel>>

    suspend fun sendMessage(
        groupChatRoomId: String,
        message: String
    ): AppResponse<ChatMessageModel>

}