package com.ntech.theyardhub.datalayer.model

data class ProductModel(
    val uuid: String = "",
    val name: String = "",
    val description: String = "",
    val price: Int = 0,
    val user: UserModel = UserModel(),
    val userReference: String = "",
    val documentId: String = "",
)
