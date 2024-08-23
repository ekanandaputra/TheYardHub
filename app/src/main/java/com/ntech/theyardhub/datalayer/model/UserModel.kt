package com.ntech.theyardhub.datalayer.model

data class UserModel(
    val id: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val yard: YardModel = YardModel(),
)
