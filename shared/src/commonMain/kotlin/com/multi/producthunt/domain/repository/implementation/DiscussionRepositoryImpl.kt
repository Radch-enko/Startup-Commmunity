package com.multi.producthunt.domain.repository.implementation

import com.multi.producthunt.domain.model.DetailDiscussionDomain
import com.multi.producthunt.domain.model.DiscussionDomain
import com.multi.producthunt.domain.model.toDomain
import com.multi.producthunt.domain.repository.DiscussionsRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.body.CreateDiscussionBody
import com.multi.producthunt.network.model.body.TopicBody
import com.multi.producthunt.network.service.ProjectsApiService
import com.multi.producthunt.network.util.asCommonFlow
import com.multi.producthunt.utils.getDeviceLang
import kotlinx.coroutines.flow.Flow

class DiscussionRepositoryImpl(
    private val service: ProjectsApiService
) : DiscussionsRepository {

    override fun getDiscussions(
        cursor: Int,
        pageSize: Int?,
        searchQuery: String?
    ): Flow<ApiResult<List<DiscussionDomain>>> {
        return service.getDiscussions(
            cursor,
            pageSize ?: 10,
            if (searchQuery.isNullOrEmpty()) emptyMap() else mapOf("searchQuery" to searchQuery)
        )
            .asCommonFlow()
            .toDomain(getDeviceLang())
    }

    override fun getDetailDiscussion(id: Int): Flow<ApiResult<DetailDiscussionDomain>> {
        return service.getDiscussionById(id).toDomain(getDeviceLang())
    }

    override fun createDiscussion(
        title: String,
        description: String,
        topics: List<Int>
    ): Flow<ApiResult<DiscussionDomain>> {
        return service.createDiscussion(
            body = CreateDiscussionBody(
                title,
                description,
                topics.map { TopicBody(it) }
            )
        ).toDomain(getDeviceLang())
    }
}


