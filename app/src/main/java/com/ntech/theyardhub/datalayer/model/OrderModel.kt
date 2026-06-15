package com.ntech.theyardhub.datalayer.model

import com.google.firebase.Timestamp

data class OrderModel(
    val orderId: String = "",
    val buyerId: String = "",
    val sellerId: String = "",
    val buyerName: String = "",
    val sellerName: String = "",
    val productName: String = "",
    val productId: String = "",
    val productImageUrl: String = "",
    val quantity: Int = 1,
    val basePrice: Int = 0,
    val shippingCost: Int = 0,
    val totalPrice: Int = 0,
    val status: String = "PENDING", // PENDING, OFFERED, ACCEPTED, CANCELLED
    val address: String = "",
    val createdAt: Timestamp = Timestamp.now(),
)
