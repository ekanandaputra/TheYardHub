package com.ntech.theyardhub.datalayer.model

data class YardModel(
    val thumbnail: String = "",
    val description: String = "",
    val locationModel: LocationModel = LocationModel(),
    val uuid: String = "",
)
