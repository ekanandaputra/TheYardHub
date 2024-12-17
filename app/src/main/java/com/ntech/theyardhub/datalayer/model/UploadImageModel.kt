package com.ntech.theyardhub.datalayer.model

data class UploadImageModel(
    val isSuccess: Boolean = false,
    val message: String = "",
    val publicUrl: String = "",
)
