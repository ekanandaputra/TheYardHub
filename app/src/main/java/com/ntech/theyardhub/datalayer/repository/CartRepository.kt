package com.ntech.theyardhub.datalayer.repository

import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.CartItemModel

interface CartRepository {
    suspend fun addToCart(item: CartItemModel): AppResponse<CartItemModel>
    suspend fun getCartItems(userId: String): AppResponse<List<CartItemModel>>
    suspend fun removeFromCart(cartItemId: String): AppResponse<String>
    suspend fun updateQuantity(cartItemId: String, quantity: Int): AppResponse<String>
    suspend fun clearCart(userId: String): AppResponse<String>
}
