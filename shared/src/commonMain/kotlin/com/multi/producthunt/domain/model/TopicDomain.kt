package com.multi.producthunt.domain.model

import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.map
import com.multi.producthunt.network.model.response.TopicResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TopicDomain(
    val id: Int,
    val title: String,
    val image: String,
    val description: String,
)

fun Flow<ApiResult<List<TopicResponse>>>.toDomain(lang: String?): Flow<ApiResult<List<TopicDomain>>> {
    return this.map { value: ApiResult<List<TopicResponse>> ->
        value.map {
            it.map { topicResponse ->
                TopicDomain(
                    topicResponse.id,
                    if (lang == "ru") topicResponse.nameRu else topicResponse.name,
                    topicResponse.image,
                    if (lang == "ru") topicResponse.descriptionRu else topicResponse.description
                )
            }
        }
    }
}