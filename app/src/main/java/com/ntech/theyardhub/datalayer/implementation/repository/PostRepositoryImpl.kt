package com.ntech.theyardhub.datalayer.implementation.repository

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
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

                        PostModel(
                            title = title,
                            content = content,
                            thumbnail = thumbnail
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
                val querySnapshot = postRef.whereEqualTo("id", "1").get().await()
                if (querySnapshot.isEmpty) {
                    return@withContext AppResponse.Empty
                } else {
                    val data = querySnapshot.documents[0]
                    val content = data.get("content") as? String ?: ""
                    val title = data.get("title") as? String ?: ""
                    val thumbnail = data.get("thumbnail") as? String ?: ""

                    return@withContext AppResponse.Success(
                        PostModel(
                            thumbnail = thumbnail,
                            title = title,
                            content = content
                        )
                    )
                }
            } catch (e: Exception) {
                return@withContext AppResponse.Error(e.toString())
            }
        }
    }

}