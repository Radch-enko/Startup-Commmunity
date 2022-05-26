package com.multi.producthunt.domain.repository.implementation

import com.multi.producthunt.domain.model.ProjectDomain
import com.multi.producthunt.domain.model.toDomain
import com.multi.producthunt.domain.repository.StartupsRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.body.AddProjectBody
import com.multi.producthunt.network.model.body.TopicBody
import com.multi.producthunt.network.service.ProjectsApiService
import com.multi.producthunt.network.util.asCommonFlow
import kotlinx.coroutines.flow.Flow

class StartupsRepositoryImpl(
    private val service: ProjectsApiService
) :
    StartupsRepository {

    override fun addProject(
        token: String?,
        name: String,
        tagline: String,
        description: String,
        thumbnail: String?,
        media: List<String?>,
        topics: List<Int>
    ): Flow<ApiResult<ProjectDomain>> {
        return service.addProject(AddProjectBody(
            name = name,
            tagline = tagline,
            description = description,
            thumbnail = thumbnail,
            media = media.filterNotNull(),
            topics = topics.map { TopicBody(it) }
        ), "Bearer " + token.orEmpty()).toDomain()
    }

    override fun getProjects(
        cursor: Int,
        pageSize: Int?,
        token: String?
    ): Flow<ApiResult<List<ProjectDomain>>> {
        return service.getProjects(
            cursor = cursor,
            pageSize = pageSize ?: 10,
            token = "Bearer " + token.orEmpty()
        ).asCommonFlow()
            .toDomain()
    }
}



