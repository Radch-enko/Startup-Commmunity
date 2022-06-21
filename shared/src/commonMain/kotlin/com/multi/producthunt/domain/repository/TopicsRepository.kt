package com.multi.producthunt.domain.repository

import com.multi.producthunt.domain.model.TopicDomain
import com.multi.producthunt.network.model.ApiResult
import kotlinx.coroutines.flow.Flow

interface TopicsRepository {
    fun getTopics(): Flow<ApiResult<List<TopicDomain>>>
    fun getDiscussionTopics(): Flow<ApiResult<List<TopicDomain>>>
}