package com.ntech.theyardhub.datalayer.implementation.repository

import com.google.firebase.firestore.CollectionReference
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.ProductModel
import com.ntech.theyardhub.datalayer.model.UserModel
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.datalayer.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRepositoryImp(
    private val userRef: CollectionReference,
    private val dataStorage: DataStorage
) : UserRepository {
    override suspend fun setUserDocumentId(userDocumentId: String) {
        dataStorage.userDocumentId = userDocumentId
    }

    override suspend fun getUserDetail(): AppResponse<UserModel>? {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = userRef.document(dataStorage.userDocumentId).get().await()
                if (!querySnapshot.exists()) {
                    return@withContext AppResponse.Empty
                } else {
                    val name: String = querySnapshot.getString("name") ?: ""
                    val yardName: String = querySnapshot.getString("yard.name") ?: ""
                    val yardDesc: String = querySnapshot.getString("yard.description") ?: ""
                    val yardThumbnail: String = querySnapshot.getString("yard.thumbnail") ?: ""

                    return@withContext AppResponse.Success(
                        UserModel(
                            name = name,
                            yard = YardModel(
                                name = yardName,
                                description = yardDesc,
                                thumbnail = yardThumbnail,
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                return@withContext AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun updateYard(request: YardModel): AppResponse<YardModel> {
        return withContext(Dispatchers.IO) {
            try {
                suspendCoroutine<AppResponse<YardModel>> { continuation ->
                    userRef.document(dataStorage.userDocumentId)
                        .update("yard", request)
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