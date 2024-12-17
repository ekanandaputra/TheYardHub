package com.ntech.theyardhub.datalayer.model

data class ProductModel(
    val name: String = "",
    val description: String = "",
    val price: Int = 0,
    val documentId: String = "",
    var imageUrl: String = "",
)
