package com.ntech.theyardhub.datalayer.repository

import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.ChatMessageModel
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.ProductModel


interface ProductRepository {

    suspend fun getProducts(): AppResponse<List<ProductModel>>

    suspend fun getUserProducts(): AppResponse<List<ProductModel>>

    suspend fun createUserProduct(request: ProductModel): AppResponse<ProductModel>

}