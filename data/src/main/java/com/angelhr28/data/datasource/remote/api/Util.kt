package com.angelhr28.data.datasource.remote.api

import com.angelhr28.domain.util.BadRequestException
import com.angelhr28.domain.util.Constant.HttpCode
import com.angelhr28.domain.util.GenericException
import retrofit2.Response

fun <T> Response<T>?.getBody(): T = when {
    this == null -> throw GenericException()
    isSuccessful -> body() ?: throw GenericException()
    code() == HttpCode.BAD_REQUEST -> throw BadRequestException(errorBody()?.string())
    else -> throw GenericException()
}
