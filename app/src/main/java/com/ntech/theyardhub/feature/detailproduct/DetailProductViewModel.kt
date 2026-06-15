package com.ntech.theyardhub.feature.detailproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.ProductModel
import com.ntech.theyardhub.datalayer.repository.ProductRepository
import kotlinx.coroutines.launch

class DetailProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _productLiveData = MutableLiveData<AppResponse<ProductModel>>()
    val productLiveData: LiveData<AppResponse<ProductModel>> get() = _productLiveData

    fun fetchProductDetail(productId: String) {
        viewModelScope.launch {
            _productLiveData.postValue(AppResponse.Loading)
            val result = productRepository.getProductDetail(productId)
            _productLiveData.postValue(result)
        }
    }
}