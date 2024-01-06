package com.angelhr28.data.datasource.remote.response

import com.google.gson.annotations.SerializedName

class LocationResponse(
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?
)
