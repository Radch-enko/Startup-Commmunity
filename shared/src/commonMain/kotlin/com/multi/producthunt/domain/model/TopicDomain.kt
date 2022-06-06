package com.multi.producthunt.domain.model

import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.map
import com.multi.producthunt.network.model.response.TopicResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TopicDomain(
    val id: Int,
    val title: String
)

fun Flow<ApiResult<List<TopicResponse>>>.toDomain(): Flow<ApiResult<List<TopicDomain>>> {
    return this.map { value: ApiResult<List<TopicResponse>> ->
        value.map {
            it.map { topicResponse ->
                TopicDomain(topicResponse.id, topicResponse.name)
            }
        }
    }
}