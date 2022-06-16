package com.multi.producthunt.domain.model

import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.map
import com.multi.producthunt.network.model.response.DetailProjectResponse
import com.multi.producthunt.network.model.response.Media
import com.multi.producthunt.network.util.CommonFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class DetailProjectDomain(
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
    val maker: UserDomain,
    val createdDate: String
)

fun Flow<ApiResult<DetailProjectResponse>>.toDomain(): Flow<ApiResult<DetailProjectDomain>> {
    return this.map { response ->
        response.map { data: DetailProjectResponse ->
            data.toDomain()
        }
    }
}

fun CommonFlow<ApiResult<List<DetailProjectResponse>>>.toDomain(): Flow<ApiResult<List<DetailProjectDomain>>> {
    return this.map { apiResult ->
        apiResult.map { listResponse ->
            listResponse.map { projectResponse ->
                projectResponse.toDomain()
            }
        }
    }
}

fun DetailProjectResponse.toDomain(): DetailProjectDomain {
    return DetailProjectDomain(
        id = this.id,
        name = this.name,
        tagline = this.tagline,
        description = this.description,
        thumbnail = this.thumbnail,
        media = this.media,
        isVoted = this.isVoted,
        topics = this.topics.map {
            TopicDomain(
                id = it.id,
                title = it.name,
                image = it.image,
                description = it.description
            )
        },
        votesCount = this.votesCount,
        ownerLink = this.ownerLink,
        comments = this.comments.map {
            CommentDomain(it.text, it.user.toDomain(), it.createdDate)
        },
        maker = this.maker.toDomain(),
        createdDate = this.createdDate
    )
}