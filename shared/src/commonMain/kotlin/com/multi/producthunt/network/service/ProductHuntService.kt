package com.multi.producthunt.network.service

import com.apollographql.apollo3.api.ApolloResponse
import com.multi.producthunt.StartupsQuery
import kotlinx.coroutines.flow.Flow

interface ProductHuntService {
    suspend fun getStartups(after: String): StartupsQuery.Data
}