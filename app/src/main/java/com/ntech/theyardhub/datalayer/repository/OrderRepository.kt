package com.ntech.theyardhub.datalayer.repository

import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.OrderModel

interface OrderRepository {
    suspend fun createOrder(order: OrderModel): AppResponse<OrderModel>
    suspend fun getOrdersByBuyer(buyerId: String): AppResponse<List<OrderModel>>
    suspend fun getOrdersBySeller(sellerId: String): AppResponse<List<OrderModel>>
    suspend fun getOrderDetail(orderId: String): AppResponse<OrderModel>
    suspend fun updateOrderStatus(orderId: String, status: String): AppResponse<String>
    suspend fun sendOffer(orderId: String, shippingCost: Int, totalPrice: Int): AppResponse<String>
}
