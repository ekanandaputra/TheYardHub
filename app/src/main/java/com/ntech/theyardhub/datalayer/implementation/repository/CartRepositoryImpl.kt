package com.ntech.theyardhub.datalayer.implementation.repository

import com.google.firebase.firestore.CollectionReference
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.CartItemModel
import com.ntech.theyardhub.datalayer.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CartRepositoryImpl(
    private val cartRef: CollectionReference
) : CartRepository {

    override suspend fun addToCart(item: CartItemModel): AppResponse<CartItemModel> {
        return withContext(Dispatchers.IO) {
            try {
                val docRef = cartRef.add(item).await()
                AppResponse.Success(item.copy(cartItemId = docRef.id))
            } catch (e: Exception) {
                AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun getCartItems(userId: String): AppResponse<List<CartItemModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = cartRef.whereEqualTo("userId", userId).get().await()
                val list = querySnapshot.documents.map { it.toObject(CartItemModel::class.java)!!.copy(cartItemId = it.id) }
                AppResponse.Success(list)
            } catch (e: Exception) {
                AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun removeFromCart(cartItemId: String): AppResponse<String> {
        return withContext(Dispatchers.IO) {
            try {
                cartRef.document(cartItemId).delete().await()
                AppResponse.Success(cartItemId)
            } catch (e: Exception) {
                AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun updateQuantity(cartItemId: String, quantity: Int): AppResponse<String> {
        return withContext(Dispatchers.IO) {
            try {
                cartRef.document(cartItemId).update("quantity", quantity).await()
                AppResponse.Success(cartItemId)
            } catch (e: Exception) {
                AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun clearCart(userId: String): AppResponse<String> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = cartRef.whereEqualTo("userId", userId).get().await()
                val batch = cartRef.firestore.batch()
                querySnapshot.documents.forEach { batch.delete(it.reference) }
                batch.commit().await()
                AppResponse.Success(userId)
            } catch (e: Exception) {
                AppResponse.Error(e.toString())
            }
        }
    }
}
