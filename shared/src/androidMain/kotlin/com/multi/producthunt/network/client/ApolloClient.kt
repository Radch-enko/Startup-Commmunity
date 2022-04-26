package com.multi.producthunt.network.client

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.network.okHttpClient
import com.multi.producthunt.BuildKonfig

actual fun apolloClient(): ApolloClient {
    return ApolloClient.Builder()
        .serverUrl(BuildKonfig.BASE_URL)
        .normalizedCache(MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024))
        .okHttpClient(getOkHttpClient(BuildKonfig.APP_ACCESS_TOKEN))
        .build()
}
