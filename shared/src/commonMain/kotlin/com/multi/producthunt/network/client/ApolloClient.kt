package com.multi.producthunt.network.client

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache

private var instance: ApolloClient? = null
private const val BASE_URL = "https://api.producthunt.com/v2/api/graphql"

fun apolloClient(): ApolloClient {
    if (instance != null) {
        return instance!!
    }

    instance = ApolloClient.Builder()
        .serverUrl(BASE_URL)
        .webSocketServerUrl(BASE_URL)
        .normalizedCache(MemoryCacheFactory(maxSizeBytes = 10 * 1024 * 1024))
        .build()

    return instance!!
}
