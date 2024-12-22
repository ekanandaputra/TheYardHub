package com.ntech.theyardhub.datalayer.implementation.repository

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.model.ChatListModel
import com.ntech.theyardhub.datalayer.model.ChatMessageModel
import com.ntech.theyardhub.datalayer.model.ChatRoomModel
import com.ntech.theyardhub.datalayer.model.ParticipantDetailModel
import com.ntech.theyardhub.datalayer.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ChatRepositoryImpl(
    private val chatRef: CollectionReference,
    private val dataStorage: DataStorage
) : ChatRepository {

    override suspend fun getChatMessage(chatRoomId: String): AppResponse<List<ChatMessageModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot =
                    chatRef.document(chatRoomId).collection("messages").get().await()
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
        message: String,
        chatRoomId: String
    ): AppResponse<ChatMessageModel> {
        val request: ChatMessageModel = ChatMessageModel(
            sender = dataStorage.userName,
            senderDocumentId = dataStorage.userDocumentId,
            content = message,
            dateTime = Timestamp.now(),
            isMyMessage = null,
        )
        return withContext(Dispatchers.IO) {
            try {
                suspendCoroutine<AppResponse<ChatMessageModel>> { continuation ->
                    chatRef.document(chatRoomId).collection("messages").add(request)
                        .addOnSuccessListener {
                            continuation.resume(AppResponse.Success(request))
                        }
                        .addOnFailureListener { e ->
                            continuation.resume(AppResponse.Error(e.toString()))
                        }
                }
            } catch (e: Exception) {
                AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun createChatRoom(request: ChatRoomModel): AppResponse<ChatRoomModel> {
        return withContext(Dispatchers.IO) {
            try {
                suspendCoroutine<AppResponse<ChatRoomModel>> { continuation ->
                    chatRef.add(request)
                        .addOnSuccessListener {
                            request.documentId = it.id
                            continuation.resume(AppResponse.Success(request))
                        }
                        .addOnFailureListener { e ->
                            continuation.resume(AppResponse.Error(e.toString()))
                        }
                }
            } catch (e: Exception) {
                AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun getChatRoomByParticipantId(participants: List<String>): AppResponse<ChatRoomModel> {
        Log.d("TAG", "getChatRoomByParticipantId: " + participants)
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot =
                    chatRef.whereEqualTo("participants", participants.sorted()).get()
                        .await()
                if (querySnapshot.isEmpty) {
                    return@withContext AppResponse.Empty
                } else {
                    val document = querySnapshot.documents.firstOrNull()

                    val documentId = document?.id ?: ""

                    Log.d("TAG", "getChatRoomByParticipantId: " + documentId)

                    return@withContext AppResponse.Success(
                        ChatRoomModel(
                            documentId = documentId
                        )
                    )
                }
            } catch (e: Exception) {
                return@withContext AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun getChatRooms(): AppResponse<List<ChatListModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot =
                    chatRef
                        .whereArrayContains("participants", dataStorage.userDocumentId)
                        .get()
                        .await()
                Log.d("TAG", "getChatRooms: " + querySnapshot.documents)
                if (querySnapshot.isEmpty) {
                    return@withContext AppResponse.Empty
                } else {
                    val list = querySnapshot.documents.map { document ->
                        val lastMessage: String = document.getString("lastMessage") ?: ""
                        val messageAt: Timestamp =
                            document.getTimestamp("lastMessageAt") ?: Timestamp(0, 0)
                        val rawParticipantDetails =
                            document.get("participantDetails") as? List<Map<String, String>>

                        val participantDetails = rawParticipantDetails?.map { entry ->
                            ParticipantDetailModel(
                                userDocumentId = entry["userDocumentId"].toString(),
                                name = entry["name"].toString()
                            )
                        }
                        Log.d("TAG", "getChatRooms: " + participantDetails)

                        val name =
                            participantDetails?.firstOrNull { it.userDocumentId != dataStorage.userDocumentId }?.name
                        ChatListModel(
                            documentId = document.id,
                            message = lastMessage,
                            messageAt = messageAt,
                            name = name ?: "",
                        )
                    }
                    return@withContext AppResponse.Success(list)
                }
            } catch (e: Exception) {
                return@withContext AppResponse.Error(e.toString())
            }
        }

    }

}