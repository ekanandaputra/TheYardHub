package com.ntech.theyardhub.feature.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.model.CartItemModel
import com.ntech.theyardhub.datalayer.repository.CartRepository
import com.ntech.theyardhub.datalayer.repository.ProductRepository
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository,
    private val dataStorage: DataStorage,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _cartItemsLiveData = MutableLiveData<AppResponse<List<CartItemModel>>>()
    val cartItemsLiveData: LiveData<AppResponse<List<CartItemModel>>> get() = _cartItemsLiveData

    private val _actionLiveData = MutableLiveData<AppResponse<String>>()
    val actionLiveData: LiveData<AppResponse<String>> get() = _actionLiveData

    fun getUserId(): String = dataStorage.userDocumentId

    fun fetchCartItems() {
        viewModelScope.launch {
            _cartItemsLiveData.postValue(AppResponse.Loading)
            val result = cartRepository.getCartItems(getUserId())
            _cartItemsLiveData.postValue(result)
        }
    }

    fun addToCart(item: CartItemModel) {
        viewModelScope.launch {
            _actionLiveData.postValue(AppResponse.Loading)
            val result = cartRepository.addToCart(item)
            if (result is AppResponse.Success) {
                _actionLiveData.postValue(AppResponse.Success("Item added to cart"))
                fetchCartItems()
            } else if (result is AppResponse.Error) {
                _actionLiveData.postValue(AppResponse.Error(result.message))
            }
        }
    }

    fun removeFromCart(cartItemId: String) {
        viewModelScope.launch {
            _actionLiveData.postValue(AppResponse.Loading)
            val result = cartRepository.removeFromCart(cartItemId)
            if (result is AppResponse.Success) {
                _actionLiveData.postValue(AppResponse.Success("Item removed from cart"))
                fetchCartItems()
            } else {
                _actionLiveData.postValue(AppResponse.Error("Failed to remove item"))
            }
        }
    }

    fun updateQuantity(cartItemId: String, quantity: Int) {
        viewModelScope.launch {
            val result = cartRepository.updateQuantity(cartItemId, quantity)
            if (result is AppResponse.Success) {
                fetchCartItems()
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            _actionLiveData.postValue(AppResponse.Loading)
            val result = cartRepository.clearCart(getUserId())
            if (result is AppResponse.Success) {
                _actionLiveData.postValue(AppResponse.Success("Cart cleared"))
                fetchCartItems()
            } else {
                _actionLiveData.postValue(AppResponse.Error("Failed to clear cart"))
            }
        }
    }
}
