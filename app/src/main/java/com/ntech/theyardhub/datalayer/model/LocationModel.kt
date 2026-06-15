package com.ntech.theyardhub.datalayer.model

data class LocationModel(
    val province: String = "",
    val city: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)
