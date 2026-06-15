package com.ntech.theyardhub.datalayer.model

data class CartItemModel(
    val cartItemId: String = "",
    val userId: String = "",
    val productId: String = "",
    val productName: String = "",
    val price: Int = 0,
    val imageUrl: String = "",
    var quantity: Int = 1,
    val sellerId: String = "",
    val sellerName: String = "",
)
