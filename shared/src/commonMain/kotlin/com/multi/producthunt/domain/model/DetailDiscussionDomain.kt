package com.multi.producthunt.domain.model

import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.map
import com.multi.producthunt.network.model.response.DetailDiscussionResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DetailDiscussionDomain(
    val id: Int,
    val title: String,
    val description: String,
    val topics: List<TopicDomain>,
    val maker: UserDomain
)

fun Flow<ApiResult<DetailDiscussionResponse>>.toDomain(deviceLang: String?): Flow<ApiResult<DetailDiscussionDomain>> {
    return this.map { response ->
        response.map { data: DetailDiscussionResponse ->
            DetailDiscussionDomain(
                id = data.id,
                title = data.title,
                description = data.description,
                topics = data.topics.map { it.toDomain(deviceLang) },
                maker = data.maker.toDomain()
            )
        }
    }
}