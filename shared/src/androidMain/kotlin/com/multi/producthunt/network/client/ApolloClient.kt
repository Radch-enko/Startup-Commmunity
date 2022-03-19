package com.multi.producthunt.network.client

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.network.okHttpClient
import com.multi.producthunt.utils.AppConfig

actual fun apolloClient(appConfig: AppConfig): ApolloClient {
    return ApolloClient.Builder()
        .serverUrl(appConfig.baseUrl)
        .normalizedCache(MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024))
        .okHttpClient(getOkHttpClient(appConfig.accessToken))
        .build()
}
