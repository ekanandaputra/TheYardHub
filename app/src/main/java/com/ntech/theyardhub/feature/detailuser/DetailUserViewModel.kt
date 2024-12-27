package com.ntech.theyardhub.feature.detailuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.ProductModel
import com.ntech.theyardhub.datalayer.model.UserModel
import com.ntech.theyardhub.datalayer.repository.ProductRepository
import com.ntech.theyardhub.datalayer.repository.UserRepository
import kotlinx.coroutines.launch

class DetailUserViewModel(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val dataStorage: DataStorage,
) : ViewModel() {

    private val _userLiveData = MutableLiveData<AppResponse<UserModel>>()
    val userLiveData: LiveData<AppResponse<UserModel>> get() = _userLiveData

    private val _productsLiveData = MutableLiveData<AppResponse<List<ProductModel>>>()
    val productsLiveData: LiveData<AppResponse<List<ProductModel>>> get() = _productsLiveData

    suspend fun fetchDetailUser() {
        viewModelScope.launch {
            _userLiveData.apply {
                postValue(AppResponse.Loading)
                val result = userRepository.getUserDetail()
                postValue(result)
            }
        }
    }

    suspend fun fetchUserProducts() {
        viewModelScope.launch {
            _productsLiveData.apply {
                postValue(AppResponse.Loading)
                val result = productRepository.getProductsByUserId(dataStorage.userDocumentId)
                postValue(result)
            }
        }
    }
}