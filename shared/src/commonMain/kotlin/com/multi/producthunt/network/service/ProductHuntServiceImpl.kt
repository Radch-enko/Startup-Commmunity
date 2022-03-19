package com.multi.producthunt.network.service

import com.apollographql.apollo3.ApolloClient
import com.multi.producthunt.StartupsQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class ProductHuntServiceImpl(private val apolloClient: ApolloClient) : ProductHuntService {
    override suspend fun getStartups(after: String): StartupsQuery.Data {
        return apolloClient.query(StartupsQuery(after)).execute().dataAssertNoErrors
    }
}