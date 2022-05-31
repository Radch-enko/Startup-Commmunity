package com.multi.producthunt.domain.model

import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.map
import com.multi.producthunt.network.model.response.Media
import com.multi.producthunt.network.model.response.ProjectResponse
import com.multi.producthunt.network.util.CommonFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class ProjectDomain(
    val id: Int,
    val name: String,
    val tagline: String,
    val description: String,
    val thumbnail: String?,
    val media: List<Media?>,
    val isVoted: Boolean,
    val topics: List<TopicDomain>,
    val votesCount: Int,
    val ownerLink: String?,
    val comments: List<CommentDomain>,
    val makerId: Int
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
        description = this.description,
        thumbnail = this.thumbnail,
        media = this.media,
        isVoted = this.isVoted,
        topics = this.topics.map { TopicDomain(it.id, it.name) },
        votesCount = this.votesCount,
        ownerLink = this.ownerLink,
        comments = this.comments.map {
            CommentDomain(it.text, it.user.toDomain(), it.createdDate)
        },
        makerId = this.makerId
    )
}
