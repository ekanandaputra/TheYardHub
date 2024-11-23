package com.ntech.theyardhub.datalayer.implementation.repository

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.model.RegisterRequest
import com.ntech.theyardhub.datalayer.model.UserModel
import com.ntech.theyardhub.datalayer.repository.AuthenticationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthenticationRepositoryImpl(
    private val authRef: CollectionReference,
    private val dataStorage: DataStorage
) : AuthenticationRepository {
    override suspend fun postLogin(
        username: String,
        password: String
    ): AppResponse<UserModel> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = authRef
                    .whereEqualTo("username", username)
                    .whereEqualTo("password", password)
                    .get().await()
                if (querySnapshot.isEmpty) {
                    return@withContext AppResponse.Empty
                } else {
                    val data = querySnapshot.documents.first()

                    val userName: String = data.get("name") as? String ?: ""
                    val userUuid: String = data.get("uuid") as? String ?: ""

                    dataStorage.userDocumentId = data.id

                    return@withContext AppResponse.Success(
                        UserModel(
                            id = userUuid,
                            name = userName
                        )
                    )
                }


            } catch (e: Exception) {
                return@withContext AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun postRegister(request: RegisterRequest): AppResponse<UserModel>? {
        val user = hashMapOf(
            "name" to request.name,
            "username" to request.username,
            "password" to request.password,
            "uuid" to request.uuid,
        )

        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = authRef
                    .whereEqualTo("username", request.username)
                    .get().await()
                Log.d("TAG", "postRegister: " + querySnapshot.documents)
                if (querySnapshot.documents.isNotEmpty()) {
                    return@withContext AppResponse.Error("Username sudah terdaftar")
                }

                suspendCancellableCoroutine { continuation ->
                    authRef.add(user)
                        .addOnSuccessListener { documentReference ->
                            continuation.resume(documentReference)
                        }
                        .addOnFailureListener { e ->
                            continuation.resumeWithException(e)
                        }
                }

                AppResponse.Success(
                    UserModel()
                )
            } catch (e: Exception) {
                return@withContext AppResponse.Error(e.toString())
            }
        }

    }
}