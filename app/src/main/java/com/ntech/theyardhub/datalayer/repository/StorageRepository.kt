package com.ntech.theyardhub.datalayer.repository

import android.net.Uri
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.UploadImageModel


interface StorageRepository {

    suspend fun uploadImage(imageUri: Uri): AppResponse<UploadImageModel>

}