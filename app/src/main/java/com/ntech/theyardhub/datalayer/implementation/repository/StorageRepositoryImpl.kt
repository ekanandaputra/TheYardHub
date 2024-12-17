package com.ntech.theyardhub.datalayer.implementation.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.LocationModel
import com.ntech.theyardhub.datalayer.model.UploadImageModel
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.datalayer.repository.StorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class StorageRepositoryImpl() : StorageRepository {

    override suspend fun uploadImage(imageUri: Uri): AppResponse<UploadImageModel> {
        return withContext(Dispatchers.IO) {
            try {
                val storage = FirebaseStorage.getInstance()
                val storageRef = storage.reference
                val imageRef =
                    storageRef.child("images/${System.currentTimeMillis()}.jpg")  // Image path
                imageRef.putFile(imageUri).await()

                val downloadUrl = imageRef.downloadUrl.await()

                return@withContext AppResponse.Success(
                    UploadImageModel(
                        isSuccess = true,
                        message = "Success Upload Image",
                        publicUrl = downloadUrl.toString()
                    )
                )

            } catch (e: Exception) {
                return@withContext AppResponse.Error(e.toString())
            }
        }
    }

}