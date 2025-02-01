package com.ntech.theyardhub.datalayer.implementation.repository

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.model.ChatListModel
import com.ntech.theyardhub.datalayer.model.ChatMessageModel
import com.ntech.theyardhub.datalayer.model.ChatRoomModel
import com.ntech.theyardhub.datalayer.model.GroupChatRoomModel
import com.ntech.theyardhub.datalayer.model.ParticipantDetailModel
import com.ntech.theyardhub.datalayer.repository.ChatRepository
import com.ntech.theyardhub.datalayer.repository.GroupChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GroupChatRepositoryImpl(
    private val groupChatRef: CollectionReference,
    private val dataStorage: DataStorage
) : GroupChatRepository {
    override suspend fun getGroupChatRooms(): AppResponse<List<GroupChatRoomModel>> {
        TODO("Not yet implemented")
    }

}