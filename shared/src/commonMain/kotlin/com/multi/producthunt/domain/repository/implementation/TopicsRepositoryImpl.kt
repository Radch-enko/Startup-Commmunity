package com.multi.producthunt.domain.repository.implementation

import com.multi.producthunt.domain.model.TopicDomain
import com.multi.producthunt.domain.model.toDomain
import com.multi.producthunt.domain.repository.TopicsRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.service.ProjectsApiService
import com.multi.producthunt.utils.getDeviceLang
import kotlinx.coroutines.flow.Flow

class TopicsRepositoryImpl(private val service: ProjectsApiService) : TopicsRepository {
    override fun getTopics(): Flow<ApiResult<List<TopicDomain>>> {
        return service.getTopics().toDomain(getDeviceLang())
    }
}