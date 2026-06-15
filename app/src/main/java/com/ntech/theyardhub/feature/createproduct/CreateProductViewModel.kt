package com.ntech.theyardhub.feature.createproduct

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
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

    val nameState = mutableStateOf(TextFieldValue(""))
    val descriptionState = mutableStateOf(TextFieldValue(""))
    val priceState = mutableStateOf(TextFieldValue(""))

    private val _createProductLiveData = MutableLiveData<AppResponse<ProductModel>>()
    val createProductLiveData: LiveData<AppResponse<ProductModel>> get() = _createProductLiveData

    suspend fun uploadImagesAndCreateProduct(imageUris: List<Uri>) {
        viewModelScope.launch {
            _createProductLiveData.postValue(AppResponse.Loading)
            try {
                val imageUrls = mutableListOf<String>()

                // Step 1: Upload all images
                for (uri in imageUris) {
                    val uploadResult = storageRepository.uploadImage(uri)
                    if (uploadResult is AppResponse.Success) {
                        imageUrls.add(uploadResult.data.publicUrl)
                    } else if (uploadResult is AppResponse.Error) {
                        _createProductLiveData.postValue(AppResponse.Error("Failed to upload an image: ${uploadResult.message}"))
                        return@launch
                    }
                }

                if (imageUrls.isEmpty()) {
                    _createProductLiveData.postValue(AppResponse.Error("At least one image is required"))
                    return@launch
                }

                // Step 2: Add imageUrl to ProductModel
                val request = ProductModel(
                    name = nameState.value.text,
                    description = descriptionState.value.text,
                    price = priceState.value.text.toIntOrNull() ?: 0,
                    imageUrl = imageUrls.first(),
                    images = imageUrls
                )

                // Step 3: Create Product
                val createProductResult = productRepository.createProduct(request)
                _createProductLiveData.postValue(createProductResult)
            } catch (e: Exception) {
                _createProductLiveData.postValue(
                    AppResponse.Error(
                        e.message ?: "An unexpected error occurred"
                    )
                )
            }
        }
    }

    fun setName(newValue: TextFieldValue) {
        nameState.value = newValue
    }

    fun setDescription(newValue: TextFieldValue) {
        descriptionState.value = newValue
    }

    fun setPrice(newValue: TextFieldValue) {
        priceState.value = newValue
    }

}