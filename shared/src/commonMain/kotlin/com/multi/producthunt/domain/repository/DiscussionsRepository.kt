package com.multi.producthunt.domain.repository

import com.multi.producthunt.domain.model.DetailDiscussionDomain
import com.multi.producthunt.domain.model.DiscussionDomain
import com.multi.producthunt.network.model.ApiResult
import kotlinx.coroutines.flow.Flow

interface DiscussionsRepository {

    fun getDiscussions(
        cursor: Int,
        pageSize: Int?,
        searchQuery: String?
    ): Flow<ApiResult<List<DiscussionDomain>>>

    fun getDetailDiscussion(
        id: Int
    ): Flow<ApiResult<DetailDiscussionDomain>>

    fun createDiscussion(
        title: String,
        description: String,
        topics: List<Int>,
    ): Flow<ApiResult<DiscussionDomain>>
}