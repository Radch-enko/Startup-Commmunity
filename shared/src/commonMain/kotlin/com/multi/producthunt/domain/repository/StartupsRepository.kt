package com.multi.producthunt.domain.repository

import com.multi.producthunt.domain.model.PostsDomain
import com.multi.producthunt.domain.model.SearchableStartupsDomain

interface StartupsRepository {
    suspend fun getTopStartups(after: String = ""): PostsDomain
    suspend fun getSearchableStartups(query: String, page: Int): SearchableStartupsDomain
}