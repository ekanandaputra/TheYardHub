package com.ntech.theyardhub.feature.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.model.OrderModel
import com.ntech.theyardhub.datalayer.repository.OrderRepository
import kotlinx.coroutines.launch

class OrderViewModel(
    private val orderRepository: OrderRepository,
    private val dataStorage: DataStorage
) : ViewModel() {

    private val _orderListLiveData = MutableLiveData<AppResponse<List<OrderModel>>>()
    val orderListLiveData: LiveData<AppResponse<List<OrderModel>>> get() = _orderListLiveData

    private val _orderDetailLiveData = MutableLiveData<AppResponse<OrderModel>>()
    val orderDetailLiveData: LiveData<AppResponse<OrderModel>> get() = _orderDetailLiveData

    private val _actionLiveData = MutableLiveData<AppResponse<String>>()
    val actionLiveData: LiveData<AppResponse<String>> get() = _actionLiveData

    fun getUserId(): String = dataStorage.userDocumentId
    fun getUserName(): String = dataStorage.userName

    fun createOrder(order: OrderModel) {
        viewModelScope.launch {
            _actionLiveData.postValue(AppResponse.Loading)
            val result = orderRepository.createOrder(order)
            if (result is AppResponse.Success) {
                _actionLiveData.postValue(AppResponse.Success("Order placed successfully"))
            } else if (result is AppResponse.Error) {
                _actionLiveData.postValue(AppResponse.Error(result.message))
            }
        }
    }

    fun fetchPurchases() {
        viewModelScope.launch {
            _orderListLiveData.postValue(AppResponse.Loading)
            val result = orderRepository.getOrdersByBuyer(dataStorage.userDocumentId)
            _orderListLiveData.postValue(result)
        }
    }

    fun fetchSales() {
        viewModelScope.launch {
            _orderListLiveData.postValue(AppResponse.Loading)
            val result = orderRepository.getOrdersBySeller(dataStorage.userDocumentId)
            _orderListLiveData.postValue(result)
        }
    }

    fun fetchOrderDetail(orderId: String) {
        viewModelScope.launch {
            _orderDetailLiveData.postValue(AppResponse.Loading)
            val result = orderRepository.getOrderDetail(orderId)
            _orderDetailLiveData.postValue(result)
        }
    }

    fun sendOffer(orderId: String, shippingCost: Int, basePrice: Int) {
        viewModelScope.launch {
            _actionLiveData.postValue(AppResponse.Loading)
            val result = orderRepository.sendOffer(orderId, shippingCost, basePrice + shippingCost)
            _actionLiveData.postValue(result)
        }
    }

    fun updateStatus(orderId: String, status: String) {
        viewModelScope.launch {
            _actionLiveData.postValue(AppResponse.Loading)
            val result = orderRepository.updateOrderStatus(orderId, status)
            _actionLiveData.postValue(result)
        }
    }
}
