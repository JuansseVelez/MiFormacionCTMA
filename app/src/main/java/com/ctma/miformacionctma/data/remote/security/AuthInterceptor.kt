package com.ctma.miformacionctma.data.remote.security

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: TokenProvider) : Interceptor {

    private val supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock_supabase_anon_key_for_evaluation"

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestOriginal = chain.request()
        val token = tokenProvider.obtenerToken() ?: supabaseKey

        val requestBuilder = requestOriginal.newBuilder()
            .header("Accept", "application/json")
            .header("apikey", supabaseKey)
            .header("Authorization", "Bearer $token")

        return chain.proceed(requestBuilder.build())
    }
}
