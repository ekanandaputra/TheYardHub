package com.ntech.theyardhub.datalayer.implementation.repository

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.core.utils.convertToInt
import com.ntech.theyardhub.datalayer.model.LocationModel
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.ProductModel
import com.ntech.theyardhub.datalayer.model.UserModel
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.datalayer.repository.PostRepository
import com.ntech.theyardhub.datalayer.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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
                        val desc = document.get("desc") as? String ?: ""
                        val name = document.get("name") as? String ?: ""
                        val price = document.get("price") as? String ?: ""

                        ProductModel(
                            name = name,
                            price = convertToInt(price) ?: 0,
                            description = desc,
                        )
                    }

                    return@withContext AppResponse.Success(list)
                }
            } catch (e: Exception) {
                return@withContext AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun createUserProduct(request: ProductModel): AppResponse<ProductModel> {
        return withContext(Dispatchers.IO) {
            try {
                suspendCoroutine<AppResponse<ProductModel>> { continuation ->
                    userRef.document(dataStorage.userDocumentId)
                        .collection("products")
                        .add(request)
                        .addOnSuccessListener {
                            continuation.resume(AppResponse.Success(request))
                        }
                        .addOnFailureListener { e ->
                            continuation.resume(AppResponse.Error(e.toString()))
                        }
                }
            } catch (e: Exception) {
                AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun createProduct(request: ProductModel): AppResponse<ProductModel> {
        return withContext(Dispatchers.IO) {
            try {
                request.userDocumentId = dataStorage.userDocumentId
                suspendCoroutine<AppResponse<ProductModel>> { continuation ->
                    productRef.add(request)
                        .addOnSuccessListener {
                            continuation.resume(AppResponse.Success(request))
                        }
                        .addOnFailureListener { e ->
                            continuation.resume(AppResponse.Error(e.toString()))
                        }
                }
            } catch (e: Exception) {
                AppResponse.Error(e.toString())
            }
        }
    }

    override suspend fun getProductsByUserId(userDocumentId: String?): AppResponse<List<ProductModel>> {
        val id = userDocumentId ?: dataStorage.userDocumentId
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot =
                    productRef.whereEqualTo("userDocumentId", id).get()
                        .await()
                if (querySnapshot.isEmpty) {
                    return@withContext AppResponse.Empty
                } else {
                    val list = querySnapshot.documents.map { document ->
                        val name: String = document.getString("name") ?: ""
                        val desc: String = document.getString("description") ?: ""
                        val price: Long = document.getLong("price") ?: 0
                        val imageUrl: String = document.getString("imageUrl") ?: ""
                        val documentId = document.id // Retrieve document ID

                        ProductModel(
                            name = name,
                            description = desc,
                            price = price.toInt(),
                            documentId = documentId,
                            imageUrl = imageUrl,
                        )
                    }
                    Log.d("TAG", "getProductsByUserId: " + list)
                    return@withContext AppResponse.Success(list)
                }
            } catch (e: Exception) {
                return@withContext AppResponse.Error(e.toString())
            }
        }

    }
}
