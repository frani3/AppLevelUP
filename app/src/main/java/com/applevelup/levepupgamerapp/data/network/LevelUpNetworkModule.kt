package com.applevelup.levepupgamerapp.data.network

import com.applevelup.levepupgamerapp.data.network.session.TokenProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object LevelUpNetworkModule {

    private const val BASE_URL = "http://localhost:8080"
    private const val TIMEOUT_SECONDS = 30L

    fun createApi(tokenProvider: TokenProvider): LevelUpMobileApi {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor(tokenProvider))
            .addInterceptor(logger)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .callTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LevelUpMobileApi::class.java)
    }
}
