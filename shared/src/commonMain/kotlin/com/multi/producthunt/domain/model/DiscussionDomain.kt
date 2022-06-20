package com.multi.producthunt.domain.model

import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.map
import com.multi.producthunt.network.model.response.DiscussionResponse
import com.multi.producthunt.network.util.CommonFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DiscussionDomain(
    val id: Int,
    val title: String,
    val description: String,
    val topics: List<TopicDomain>,
    val maker: UserDomain
)

fun Flow<ApiResult<DiscussionResponse>>.toDomain(lang: String?): Flow<ApiResult<DiscussionDomain>> {
    return this.map { response ->
        response.map {
            it.toDomain(lang)
        }
    }
}

fun CommonFlow<ApiResult<List<DiscussionResponse>>>.toDomain(lang: String?): Flow<ApiResult<List<DiscussionDomain>>> {
    return this.map { apiResult ->
        apiResult.map { listResponse ->
            listResponse.map { projectResponse ->
                projectResponse.toDomain(lang)
            }
        }
    }
}

fun DiscussionResponse.toDomain(lang: String?): DiscussionDomain {
    return DiscussionDomain(
        id = this.id,
        title = this.title,
        description = this.description,
        topics = this.topics.map { it.toDomain(lang) },
        maker = this.maker.toDomain()
    )
}
