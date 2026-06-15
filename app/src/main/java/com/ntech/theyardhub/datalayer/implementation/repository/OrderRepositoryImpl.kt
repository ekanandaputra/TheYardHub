package com.ntech.theyardhub.datalayer.implementation.repository

import com.google.firebase.firestore.CollectionReference
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.model.OrderModel
import com.ntech.theyardhub.datalayer.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class OrderRepositoryImpl(
    private val orderRef: CollectionReference,
    private val dataStorage: DataStorage
) : OrderRepository {

    override suspend fun createOrder(order: OrderModel): AppResponse<OrderModel> {
        return withContext(Dispatchers.IO) {
            try {
                val docRef = orderRef.add(order).await()
                AppResponse.Success(order.copy(orderId = docRef.id))
            } catch (e: Exception) {
                AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun getOrdersByBuyer(buyerId: String): AppResponse<List<OrderModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = orderRef.whereEqualTo("buyerId", buyerId).get().await()
                val list = querySnapshot.documents.map { it.toObject(OrderModel::class.java)!!.copy(orderId = it.id) }
                AppResponse.Success(list)
            } catch (e: Exception) {
                AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun getOrdersBySeller(sellerId: String): AppResponse<List<OrderModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = orderRef.whereEqualTo("sellerId", sellerId).get().await()
                val list = querySnapshot.documents.map { it.toObject(OrderModel::class.java)!!.copy(orderId = it.id) }
                AppResponse.Success(list)
            } catch (e: Exception) {
                AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun getOrderDetail(orderId: String): AppResponse<OrderModel> {
        return withContext(Dispatchers.IO) {
            try {
                val document = orderRef.document(orderId).get().await()
                val order = document.toObject(OrderModel::class.java)!!.copy(orderId = document.id)
                AppResponse.Success(order)
            } catch (e: Exception) {
                AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun updateOrderStatus(orderId: String, status: String): AppResponse<String> {
        return withContext(Dispatchers.IO) {
            try {
                orderRef.document(orderId).update("status", status).await()
                AppResponse.Success(status)
            } catch (e: Exception) {
                AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun sendOffer(orderId: String, shippingCost: Int, totalPrice: Int): AppResponse<String> {
        return withContext(Dispatchers.IO) {
            try {
                orderRef.document(orderId).update(
                    mapOf(
                        "shippingCost" to shippingCost,
                        "totalPrice" to totalPrice,
                        "status" to "OFFERED"
                    )
                ).await()
                AppResponse.Success("OFFERED")
            } catch (e: Exception) {
                AppResponse.Error(e.toString())
            }
        }
    }
}
