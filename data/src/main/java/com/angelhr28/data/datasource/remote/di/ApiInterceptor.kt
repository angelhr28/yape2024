package com.angelhr28.data.datasource.remote.di

import com.angelhr28.domain.util.Constant
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ApiInterceptor @Inject constructor(
    @Named("appVersion") private val appVersion: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request().newBuilder().let {
            it.header(Constant.Header.X_PLATFORM, Constant.Header.PLATFORM)
            it.header(Constant.Header.X_VERSION, appVersion)
            it.build()
        }
        return chain.proceed(request)
    }
}
