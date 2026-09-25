package com.ctma.miformacionctma.data.remote.security

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: TokenProvider) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestOriginal = chain.request()
        val token = tokenProvider.obtenerToken()

        val requestBuilder = requestOriginal.newBuilder()
            .header("Accept", "application/json")

        if (!token.isNullOrEmpty()) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
