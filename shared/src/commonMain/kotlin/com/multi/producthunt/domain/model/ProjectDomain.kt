package com.multi.producthunt.domain.model

import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.map
import com.multi.producthunt.network.model.response.ProjectResponse
import com.multi.producthunt.network.util.CommonFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class ProjectDomain(
    val id: Int,
    val name: String,
    val tagline: String,
    val thumbnail: String?,
    val isVoted: Boolean,
    val topics: List<TopicDomain>,
    val votesCount: Int,
)

fun Flow<ApiResult<ProjectResponse>>.toDomain(): Flow<ApiResult<ProjectDomain>> {
    return this.map { response ->
        response.map { data: ProjectResponse ->
            data.toDomain()
        }
    }
}

fun CommonFlow<ApiResult<List<ProjectResponse>>>.toDomain(): Flow<ApiResult<List<ProjectDomain>>> {
    return this.map { apiResult ->
        apiResult.map { listResponse ->
            listResponse.map { projectResponse ->
                projectResponse.toDomain()
            }
        }
    }
}

fun ProjectResponse.toDomain(): ProjectDomain {
    return ProjectDomain(
        id = this.id,
        name = this.name,
        tagline = this.tagline,
        thumbnail = this.thumbnail,
        isVoted = this.isVoted,
        topics = this.topics.map { TopicDomain(it.id, it.name, it.image, it.description) },
        votesCount = this.votesCount,
    )
}
