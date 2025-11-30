package com.applevelup.levepupgamerapp.data.network

import com.applevelup.levepupgamerapp.data.network.session.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(private val tokenProvider: TokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider.getToken()
        val request = chain.request()
        val authenticatedRequest = if (!token.isNullOrBlank()) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }
        return chain.proceed(authenticatedRequest)
    }
}
