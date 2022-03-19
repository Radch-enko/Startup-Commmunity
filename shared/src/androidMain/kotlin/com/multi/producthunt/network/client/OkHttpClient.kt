package com.multi.producthunt.network.client

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

private var okHttpClient: OkHttpClient? = null

fun getOkHttpClient(accessToken: String): OkHttpClient {
    return if (okHttpClient == null) {
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header(
                    "Authorization",
                    "Bearer $accessToken"
                )
                chain.proceed(builder.build())
            }
            .build()
    } else okHttpClient!!

}