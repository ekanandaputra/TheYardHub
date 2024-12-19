package com.ntech.theyardhub.feature.registeryard

import android.net.Uri
import android.util.Log
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
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.datalayer.repository.ProductRepository
import com.ntech.theyardhub.datalayer.repository.StorageRepository
import com.ntech.theyardhub.datalayer.repository.UserRepository
import com.ntech.theyardhub.datalayer.repository.YardRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterYardViewModel(
    private val userRepository: UserRepository,
    private val storageRepository: StorageRepository,
    private val yardRepository: YardRepository,
) : ViewModel() {

    val nameState = mutableStateOf(TextFieldValue(""))
    val descriptionState = mutableStateOf(TextFieldValue(""))

    private val _userLiveData = MutableLiveData<AppResponse<UserModel>>()
    val userLiveData: LiveData<AppResponse<UserModel>> get() = _userLiveData

    private val _updateYardLiveData = MutableLiveData<AppResponse<YardModel>>()
    val updateYardLiveData: LiveData<AppResponse<YardModel>> get() = _updateYardLiveData

    suspend fun fetchDetailUser() {
        viewModelScope.launch {
            _userLiveData.apply {
                postValue(AppResponse.Loading)
                val result = userRepository.getUserDetail()
                postValue(result)
            }
        }
    }

    fun setName(newValue: TextFieldValue) {
        nameState.value = newValue
    }

    fun setDescription(newValue: TextFieldValue) {
        descriptionState.value = newValue
    }

    suspend fun updateYard(imageUri: Uri) {
        viewModelScope.launch {
            _updateYardLiveData.postValue(AppResponse.Loading)
            try {
                // Step 1: Upload Image
                val uploadResult = storageRepository.uploadImage(imageUri)
                if (uploadResult is AppResponse.Success) {
                    val imageUrl = uploadResult.data // Assume `data` contains the image URL

                    // Step 2: Add imageUrl to ProductModel
                    val request = YardModel(
                        name = nameState.value.text,
                        description = descriptionState.value.text,
                        thumbnail = imageUrl.publicUrl
                    )

                    val yard = yardRepository.getFarmByUserId()
                    Log.d("TAG", "updateYard: " + yard);
                    if (yard is AppResponse.Empty) {
                        Log.d("TAG", "updateYard: Create Farm")
                        yardRepository.createFarm(request)
                    } else {
                        when (yard) {
                            is AppResponse.Success -> {
                                Log.d("TAG", "updateYard: Update Farm " + yard)
                                yardRepository.updateFarm(yard.data.documentId, request)
                            }

                            else -> {

                            }
                        }
                    }

                    // Step 3: Create Product
                    val updateYardResult = userRepository.updateYard(request)
                    _updateYardLiveData.postValue(updateYardResult)

                } else if (uploadResult is AppResponse.Error) {
                    _updateYardLiveData.postValue(AppResponse.Error(uploadResult.message))
                }
            } catch (e: Exception) {
                _updateYardLiveData.postValue(
                    AppResponse.Error(
                        e.message ?: "An unexpected error occurred"
                    )
                )
            }
        }
    }

}