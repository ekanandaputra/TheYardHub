package com.ntech.theyardhub.feature.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.ProductModel
import com.ntech.theyardhub.datalayer.repository.PostRepository
import com.ntech.theyardhub.datalayer.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val _productLiveData = MutableLiveData<AppResponse<List<ProductModel>>>()
    val productLiveData: LiveData<AppResponse<List<ProductModel>>> get() = _productLiveData

    suspend fun fetchProduct() {
        viewModelScope.launch {
            _productLiveData.apply {
                postValue(AppResponse.Loading)
                val result = productRepository.getProducts()
                postValue(result)
            }
        }
    }
}