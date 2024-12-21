package com.ntech.theyardhub.datalayer.implementation.repository

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.model.ChatMessageModel
import com.ntech.theyardhub.datalayer.model.ChatRoomModel
import com.ntech.theyardhub.datalayer.model.ProductModel
import com.ntech.theyardhub.datalayer.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ChatRepositoryImpl(
    private val chatRef: CollectionReference,
    private val dataStorage: DataStorage
) : ChatRepository {

    override suspend fun getChatMessage(): AppResponse<List<ChatMessageModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = chatRef.get().await()
                if (querySnapshot.isEmpty) {
                    return@withContext AppResponse.Empty
                } else {
                    val list = querySnapshot.documents.map { document ->
                        val dateTime =
                            (document.get("dateTime") as? Timestamp)?.let { timestampToString(it) }
                        val sender = document.get("sender") as? String ?: ""
                        val content = document.get("message") as? String ?: ""
                        val isMyMessage = sender == "ekananda"

                        ChatMessageModel(
                            sender = sender,
                            content = content,
                            dateTime = dateTime ?: "",
                            isMyMessage = true,
                        )
                    }
                    return@withContext AppResponse.Success(list)
                }
            } catch (e: Exception) {
                return@withContext AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun sendMessage(message: String): AppResponse<ChatMessageModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getLatestChats(): AppResponse<List<ChatMessageModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = chatRef.get().await()
                if (querySnapshot.isEmpty) {
                    return@withContext AppResponse.Empty
                } else {
                    val list = querySnapshot.documents.map { document ->
                        val dateTime =
                            (document.get("dateTime") as? Timestamp)?.let { timestampToString(it) }
                        val sender = document.get("sender") as? String ?: ""
                        val content = document.get("message") as? String ?: ""
                        val isMyMessage = sender == "ekananda"

                        ChatMessageModel(
                            sender = sender,
                            content = content,
                            dateTime = dateTime ?: "",
                            isMyMessage = true,
                        )
                    }
                    return@withContext AppResponse.Success(list)
                }
            } catch (e: Exception) {
                return@withContext AppResponse.Error(e.toString())
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

    private fun timestampToString(timestamp: Timestamp): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = timestamp.toDate()
        return dateFormat.format(date)
    }

}