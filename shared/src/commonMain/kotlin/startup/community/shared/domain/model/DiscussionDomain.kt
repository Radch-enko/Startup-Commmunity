package startup.community.shared.domain.model

import startup.community.shared.network.model.ApiResult
import startup.community.shared.network.model.map
import startup.community.shared.network.model.response.DiscussionResponse
import startup.community.shared.network.util.CommonFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DiscussionDomain(
    val id: Int,
    val title: String,
    val topics: List<TopicDomain>,
    val maker: UserDomain,
    val createdDate: String,
    val replies: Int
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
        topics = this.topics.map { it.toDomain(lang) },
        maker = this.maker.toDomain(),
        createdDate = this.createdDate,
        replies = this.replies
    )
}
