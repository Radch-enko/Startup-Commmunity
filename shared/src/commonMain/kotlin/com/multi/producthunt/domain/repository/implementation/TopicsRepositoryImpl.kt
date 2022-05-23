package com.multi.producthunt.domain.repository.implementation

import com.multi.producthunt.domain.model.TopicDomain
import com.multi.producthunt.domain.repository.TopicsRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.map
import com.multi.producthunt.network.model.response.TopicResponse
import com.multi.producthunt.network.service.ProjectsApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TopicsRepositoryImpl(private val service: ProjectsApiService) : TopicsRepository {
    override fun getTopics(): Flow<ApiResult<List<TopicDomain>>> {
        return service.getTopics().toDomain()
    }
}

private fun Flow<ApiResult<List<TopicResponse>>>.toDomain(): Flow<ApiResult<List<TopicDomain>>> {
    return this.map { value: ApiResult<List<TopicResponse>> ->
        value.map {
            it.map { topicResponse ->
                TopicDomain(topicResponse.id, topicResponse.name)
            }
        }
    }
}
