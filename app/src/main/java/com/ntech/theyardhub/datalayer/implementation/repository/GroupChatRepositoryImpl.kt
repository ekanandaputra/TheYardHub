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
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot =
                    groupChatRef
                        .get()
                        .await()
                Log.d("TAG", "getGroupChatRooms: " + querySnapshot.documents)
                if (querySnapshot.isEmpty) {
                    return@withContext AppResponse.Empty
                } else {
                    val list = querySnapshot.documents.map { document ->
                        val name: String = document.getString("name") ?: ""
                        val documentId = document.id // Retrieve document ID

                        GroupChatRoomModel(
                            documentId = documentId,
                            name = name ?: "",
                        )
                    }
                    return@withContext AppResponse.Success(list)
                }
            } catch (e: Exception) {
                Log.e("TAG", "getGroupChatRooms: " + e.toString())
                return@withContext AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun getMessages(groupChatRoomId: String): AppResponse<List<ChatMessageModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot =
                    groupChatRef.document(groupChatRoomId).collection("messages")
//                        .orderBy("dateTime", Query.Direction.ASCENDING)
                        .get().await()
                if (querySnapshot.isEmpty) {
                    return@withContext AppResponse.Empty
                } else {
                    val list = querySnapshot.documents.map { document ->

                        val sender: String = document.getString("sender") ?: ""
                        val senderDocumentId: String = document.getString("senderDocumentId") ?: ""
                        val content: String = document.getString("content") ?: ""
                        val dateTime: Timestamp =
                            document.getTimestamp("dateTime") ?: Timestamp(0, 0)
                        val isMyMessage = senderDocumentId == dataStorage.userDocumentId

                        ChatMessageModel(
                            sender = sender,
                            senderDocumentId = senderDocumentId,
                            content = content,
                            dateTime = dateTime,
                            isMyMessage = isMyMessage,
                        )
                    }
                    return@withContext AppResponse.Success(list)
                }
            } catch (e: Exception) {
                return@withContext AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun sendMessage(
        groupChatRoomId: String,
        message: String
    ): AppResponse<ChatMessageModel> {
        val request: ChatMessageModel = ChatMessageModel(
            sender = dataStorage.userName,
            senderDocumentId = dataStorage.userDocumentId,
            content = message,
            dateTime = Timestamp.now(),
            isMyMessage = null,
        )

        val lastMessageRequest = mapOf(
            "lastMessage" to message,
            "lastMessageAt" to Timestamp.now()
        )

        return withContext(Dispatchers.IO) {
            try {
                val messageRef =
                    groupChatRef.document(groupChatRoomId).collection("messages").add(request)
                        .await()
                AppResponse.Success(request)
            } catch (e: Exception) {
                AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun createChatRoom(roomName: String): AppResponse<GroupChatRoomModel> {
        val request: GroupChatRoomModel = GroupChatRoomModel(
            name = roomName,
            documentId = "",
        )

        return withContext(Dispatchers.IO) {
            try {
                val messageRef =
                    groupChatRef.add(request)
                        .await()
                AppResponse.Success(request)
            } catch (e: Exception) {
                AppResponse.Error(e.toString())
            }
        }
    }
}