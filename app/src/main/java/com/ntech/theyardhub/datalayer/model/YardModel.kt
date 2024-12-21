package com.ntech.theyardhub.datalayer.model

data class YardModel(
    val name: String = "",
    val thumbnail: String = "",
    val description: String = "",
    val locationModel: LocationModel = LocationModel(),
    val documentId: String = "",
    var userDocumentId: String = "",
    var ownerName: String = "",
)
