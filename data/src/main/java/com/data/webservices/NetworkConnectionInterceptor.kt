package com.data.webservices

import androidx.annotation.NonNull
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


abstract class NetworkConnectionInterceptor : Interceptor {

    abstract val isInternetAvailable: Boolean

    abstract fun onInternetUnavailable()

    @Throws(IOException::class)
    override fun intercept(@NonNull chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!isInternetAvailable) {
            onInternetUnavailable()
            //throw NoNetworkException()
        }
        return chain.proceed(request)
    }

    class NoNetworkException internal constructor() : RuntimeException("Please check Network Connection")

}