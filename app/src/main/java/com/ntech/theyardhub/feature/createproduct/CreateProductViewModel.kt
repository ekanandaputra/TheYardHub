package com.ntech.theyardhub.feature.createproduct

import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.ProductModel
import com.ntech.theyardhub.datalayer.model.UploadImageModel
import com.ntech.theyardhub.datalayer.model.UserModel
import com.ntech.theyardhub.datalayer.repository.ProductRepository
import com.ntech.theyardhub.datalayer.repository.StorageRepository
import com.ntech.theyardhub.datalayer.repository.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CreateProductViewModel(
    private val storageRepository: StorageRepository
) : ViewModel() {


//    suspend fun uploadImageToFirebase(imageUri: Uri) {
//        try {
//            val storage = FirebaseStorage.getInstance()
//            val storageRef = storage.reference
//            val imageRef =
//                storageRef.child("images/${System.currentTimeMillis()}.jpg")  // Image path
//
//            imageRef.putFile(imageUri).await()  // Upload the image
////            val downloadUrl = imageRef.downloadUrl.await()  // Get the download URL
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    private val _uploadImageLiveData = MutableLiveData<AppResponse<UploadImageModel>>()
    val uploadImageLiveData: LiveData<AppResponse<UploadImageModel>> get() = _uploadImageLiveData

    suspend fun uploadImage(imageUri: Uri) {
        viewModelScope.launch {
            _uploadImageLiveData.apply {
                postValue(AppResponse.Loading)
                val result = storageRepository.uploadImage(imageUri)
                postValue(result)
            }
        }
    }

}