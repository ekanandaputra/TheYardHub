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

    suspend fun uploadImageAndCreateProduct(imageUri: Uri) {
        viewModelScope.launch {
            _createProductLiveData.postValue(AppResponse.Loading)
            try {
                // Step 1: Upload Image
                val uploadResult = storageRepository.uploadImage(imageUri)
                if (uploadResult is AppResponse.Success) {
                    val imageUrl = uploadResult.data // Assume `data` contains the image URL

                    // Step 2: Add imageUrl to ProductModel
                    val request = ProductModel(
                        name = nameState.value.text,
                        description = descriptionState.value.text,
                        price = priceState.value.text.toInt(),
                        imageUrl = imageUrl.publicUrl,
                    )

                    // Step 3: Create Product
                    val createProductResult = productRepository.createProduct(request)
                    _createProductLiveData.postValue(createProductResult)
                } else if (uploadResult is AppResponse.Error) {
                    _createProductLiveData.postValue(AppResponse.Error(uploadResult.message))
                }
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