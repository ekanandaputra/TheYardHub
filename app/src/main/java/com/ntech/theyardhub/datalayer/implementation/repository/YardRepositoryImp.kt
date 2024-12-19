package com.ntech.theyardhub.datalayer.implementation.repository

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.model.LocationModel
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.UserModel
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.datalayer.repository.PostRepository
import com.ntech.theyardhub.datalayer.repository.ProductRepository
import com.ntech.theyardhub.datalayer.repository.YardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.log

class YardRepositoryImp(
    private val yardRef: CollectionReference,
    private val userRef: CollectionReference,
    private val dataStorage: DataStorage
) : YardRepository {
    override suspend fun getFarms(): AppResponse<List<YardModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = yardRef.get().await()
                if (querySnapshot.isEmpty) {
                    return@withContext AppResponse.Empty
                } else {
                    val list = querySnapshot.documents.map { document ->
                        val name = document.get("name") as? String ?: ""
                        val desc = document.get("desc") as? String ?: ""
                        val city = document.get("city") as? String ?: ""
                        val documentId = document.id // Retrieve document ID

                        YardModel(
                            name = name,
                            description = desc,
                            locationModel = LocationModel(city = city),
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

    override suspend fun getFarm(documentId: String): AppResponse<YardModel> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = yardRef.document(documentId).get().await()
                if (!querySnapshot.exists()) {
                    return@withContext AppResponse.Empty
                } else {
                    val name: String = querySnapshot.getString("name") ?: ""
                    val desc: String = querySnapshot.getString("description") ?: ""
                    val city: String = querySnapshot.getString("city") ?: ""

                    return@withContext AppResponse.Success(
                        YardModel(
                            name = name,
                            description = desc,
                            locationModel = LocationModel(city = city),
                            documentId = documentId,
                        )
                    )
                }
            } catch (e: Exception) {
                return@withContext AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun getFarmByUserId(): AppResponse<YardModel> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot =
                    yardRef.whereEqualTo("userDocumentId", dataStorage.userDocumentId).get().await()
                if (querySnapshot.isEmpty) {
                    return@withContext AppResponse.Empty
                } else {
                    val document = querySnapshot.documents.firstOrNull()

                    // Extract all required fields
                    val name: String = document?.getString("name") ?: ""
                    val thumbnail: String = document?.getString("thumbnail") ?: ""
                    val description: String = document?.getString("description") ?: ""
                    val documentId: String = document?.id ?: ""
                    val userDocumentId: String = document?.getString("userDocumentId") ?: ""
                    val locationData = document?.get("locationModel") as? Map<*, *>

                    // Map the locationModel if it exists
                    val locationModel = if (locationData != null) {
                        LocationModel(
                            latitude = locationData["latitude"] as? Double ?: 0.0,
                            longitude = locationData["longitude"] as? Double ?: 0.0
                        )
                    } else {
                        LocationModel()
                    }

                    // Return the fully constructed YardModel
                    return@withContext AppResponse.Success(
                        YardModel(
                            name = name,
                            thumbnail = thumbnail,
                            description = description,
                            locationModel = locationModel,
                            documentId = documentId,
                            userDocumentId = userDocumentId
                        )
                    )
                }
            } catch (e: Exception) {
                return@withContext AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun createFarm(request: YardModel): AppResponse<YardModel> {
        return withContext(Dispatchers.IO) {
            try {
                request.userDocumentId = dataStorage.userDocumentId
                suspendCoroutine<AppResponse<YardModel>> { continuation ->
                    yardRef.add(request)
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

    override suspend fun updateFarm(
        documentId: String,
        request: YardModel
    ): AppResponse<YardModel> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("TAG", "updateFarm: " + documentId)
                request.userDocumentId = dataStorage.userDocumentId
                suspendCoroutine<AppResponse<YardModel>> { continuation ->
                    yardRef.document(documentId)
                        .set(request, SetOptions.merge())
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

}