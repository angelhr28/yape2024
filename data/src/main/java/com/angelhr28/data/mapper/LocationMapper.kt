package com.angelhr28.data.mapper

import com.angelhr28.data.datasource.remote.response.LocationResponse
import com.angelhr28.domain.model.Location
import com.angelhr28.domain.util.Constant.Number

fun LocationResponse.toDomain() = Location(
    latitude = latitude ?: Number.ZERO_DEC,
    longitude = longitude ?: Number.ZERO_DEC
)
