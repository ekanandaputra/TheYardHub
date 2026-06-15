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
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.LocationModel
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
    val locationState = mutableStateOf(LocationModel())

    private val _userLiveData = MutableLiveData<AppResponse<UserModel>>()
    val userLiveData: LiveData<AppResponse<UserModel>> get() = _userLiveData

    private val _updateYardLiveData = MutableLiveData<AppResponse<YardModel>>()
    val updateYardLiveData: LiveData<AppResponse<YardModel>> get() = _updateYardLiveData

    suspend fun fetchDetailUser() {
        viewModelScope.launch {
            _userLiveData.apply {
                postValue(AppResponse.Loading)
                val result = userRepository.getUserDetail()
                if (result is AppResponse.Success) {
                    locationState.value = result.data.yard.locationModel
                }
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

    fun setLocation(latitude: Double, longitude: Double) {
        locationState.value = locationState.value.copy(latitude = latitude, longitude = longitude)
    }

    fun setProvince(province: String) {
        locationState.value = locationState.value.copy(province = province, city = "")
    }

    fun setCity(city: String) {
        locationState.value = locationState.value.copy(city = city)
    }

    suspend fun updateYard(imageUri: Uri?) {
        viewModelScope.launch {
            _updateYardLiveData.postValue(AppResponse.Loading)
            try {
                var imageUrl = ""

                // Step 1: Upload Image if selected
                if (imageUri != null && !imageUri.toString().startsWith("http")) {
                    val uploadResult = storageRepository.uploadImage(imageUri)
                    if (uploadResult is AppResponse.Success) {
                        imageUrl = uploadResult.data.publicUrl
                    } else if (uploadResult is AppResponse.Error) {
                        _updateYardLiveData.postValue(AppResponse.Error(uploadResult.message))
                        return@launch
                    }
                } else if (imageUri != null) {
                    imageUrl = imageUri.toString()
                }

                // Step 2: Prepare Request
                val request = YardModel(
                    name = nameState.value.text,
                    description = descriptionState.value.text,
                    thumbnail = imageUrl,
                    locationModel = locationState.value
                )

                val yard = yardRepository.getFarmByUserId()
                if (yard is AppResponse.Empty) {
                    yardRepository.createFarm(request)
                } else if (yard is AppResponse.Success) {
                    yardRepository.updateFarm(yard.data.documentId, request)
                }

                val updateYardResult = userRepository.updateYard(request)
                _updateYardLiveData.postValue(updateYardResult)

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