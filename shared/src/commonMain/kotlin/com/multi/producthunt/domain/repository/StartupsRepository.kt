package com.multi.producthunt.domain.repository

import com.multi.producthunt.domain.model.PostsDomain
import com.multi.producthunt.domain.model.ProjectDomain
import com.multi.producthunt.domain.model.SearchableStartupsDomain
import com.multi.producthunt.network.model.ApiResult
import kotlinx.coroutines.flow.Flow

interface StartupsRepository {
    suspend fun getTopStartups(after: String = ""): PostsDomain
    suspend fun getSearchableStartups(query: String, page: Int): SearchableStartupsDomain

    fun addProject(
        token: String?,
        name: String,
        tagline: String,
        description: String,
        thumbnail: String? = null,
        media: List<String?> = emptyList(),
        topics: List<Int>
    ): Flow<ApiResult<ProjectDomain>>
}