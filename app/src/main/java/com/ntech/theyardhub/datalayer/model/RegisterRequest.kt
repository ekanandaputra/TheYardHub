package com.ntech.theyardhub.datalayer.model

data class RegisterRequest(
    val uuid: String = "",
    val name: String = "",
    val username: String = "",
    val password: String = "",
)
