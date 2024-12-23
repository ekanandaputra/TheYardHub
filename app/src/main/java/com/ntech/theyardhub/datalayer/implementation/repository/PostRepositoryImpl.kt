package com.ntech.theyardhub.datalayer.implementation.repository

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.model.ChatMessageModel
import com.ntech.theyardhub.datalayer.model.DiscussionModel
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PostRepositoryImpl(
    private val postRef: CollectionReference,
    private val dataStorage: DataStorage
) : PostRepository {

    override suspend fun getPosts(): AppResponse<List<PostModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = postRef.get().await()
                if (querySnapshot.isEmpty) {
                    return@withContext AppResponse.Empty
                } else {
                    val list = querySnapshot.documents.map { document ->
                        val content = document.get("content") as? String ?: ""
                        val title = document.get("title") as? String ?: ""
                        val thumbnail = document.get("thumbnail") as? String ?: ""
                        val documentId = document.id // Retrieve document ID

                        PostModel(
                            title = title,
                            content = content,
                            thumbnail = thumbnail,
                            documentId = documentId,
                        )
                    }
                    return@withContext AppResponse.Success(list)
                }
            } catch (e: Exception) {
                return@withContext AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun getPost(id: String): AppResponse<PostModel> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = postRef.document(id).get().await()
                if (!querySnapshot.exists()) {
                    return@withContext AppResponse.Empty
                } else {
                    val title: String = querySnapshot.getString("title") ?: ""
                    val content: String = querySnapshot.getString("content") ?: ""
                    val thumbnail: String = querySnapshot.getString("thumbnail") ?: ""

                    return@withContext AppResponse.Success(
                        PostModel(
                            thumbnail = thumbnail,
                            title = title,
                            content = content,
                            documentId = id,
                        )
                    )
                }
            } catch (e: Exception) {
                return@withContext AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun getDiscussion(postDocumentId: String): AppResponse<List<DiscussionModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot =
                    postRef.document(postDocumentId).collection("discussions")
                        .get().await()
                Log.d("TAG", "getDiscussion: " + querySnapshot.documents)
                Log.d("TAG", "getDiscussion: " + postDocumentId)

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

                        DiscussionModel(
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

    override suspend fun sendDiscussion(
        postDocumentId: String,
        message: String
    ): AppResponse<DiscussionModel> {
        val request: DiscussionModel = DiscussionModel(
            sender = dataStorage.userName,
            senderDocumentId = dataStorage.userDocumentId,
            content = message,
            dateTime = Timestamp.now(),
            isMyMessage = null,
        )
        return withContext(Dispatchers.IO) {
            try {
                val messageRef =
                    postRef.document(postDocumentId).collection("discussions").add(request).await()
                AppResponse.Success(request)
            } catch (e: Exception) {
                AppResponse.Error(e.toString())
            }
        }
    }

}