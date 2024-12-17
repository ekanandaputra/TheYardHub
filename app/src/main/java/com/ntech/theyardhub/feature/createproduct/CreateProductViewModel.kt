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
    private val storageRepository: StorageRepository,
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val _createProductLiveData = MutableLiveData<AppResponse<ProductModel>>()
    val createProductLiveData: LiveData<AppResponse<ProductModel>> get() = _createProductLiveData

    suspend fun uploadImageAndCreateProduct(imageUri: Uri, product: ProductModel) {
        viewModelScope.launch {
            _createProductLiveData.postValue(AppResponse.Loading)
            try {
                // Step 1: Upload Image
                val uploadResult = storageRepository.uploadImage(imageUri)
                if (uploadResult is AppResponse.Success) {
                    val imageUrl = uploadResult.data // Assume `data` contains the image URL

                    // Step 2: Add imageUrl to ProductModel
                    val updatedProduct = product.copy(imageUrl = imageUrl.publicUrl)

                    // Step 3: Create Product
                    val createProductResult = productRepository.createUserProduct(updatedProduct)
                    _createProductLiveData.postValue(createProductResult)
                } else if (uploadResult is AppResponse.Error) {
                    _createProductLiveData.postValue(AppResponse.Error(uploadResult.message))
                }
            } catch (e: Exception) {
                _createProductLiveData.postValue(AppResponse.Error(e.message ?: "An unexpected error occurred"))
            }
        }
    }

}