package com.ntech.theyardhub.datalayer.implementation.repository

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.core.utils.convertToInt
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.ProductModel
import com.ntech.theyardhub.datalayer.model.UserModel
import com.ntech.theyardhub.datalayer.repository.PostRepository
import com.ntech.theyardhub.datalayer.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val productRef: CollectionReference,
    private val userRef: CollectionReference,
    private val dataStorage: DataStorage
) : ProductRepository {
    override suspend fun getProducts(): AppResponse<List<ProductModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = productRef.whereEqualTo("user.uuid", "123213")
                    .get().await()
                if (querySnapshot.isEmpty) {
                    return@withContext AppResponse.Empty
                } else {
                    val list = querySnapshot.documents.map { document ->
                        val uuid = document.get("uuid") as? String ?: ""
                        val name = document.get("name") as? String ?: ""
                        val price = document.get("price") as? String ?: ""
                        val userReference = document.get("userReference") as? String ?: ""
                        val userName = document.get("user.name") as? String ?: ""

                        ProductModel(
                            uuid = uuid,
                            name = name,
                            price = convertToInt(price) ?: 0,
                            userReference = userReference,
                            user = UserModel(name = userName)
                        )
                    }

                    return@withContext AppResponse.Success(list)
                }
            } catch (e: Exception) {
                return@withContext AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun getUserProducts(): AppResponse<List<ProductModel>> {
        TODO("Not yet implemented")
    }

}