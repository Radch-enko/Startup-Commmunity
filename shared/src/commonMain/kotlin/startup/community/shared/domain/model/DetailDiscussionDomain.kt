package startup.community.shared.domain.model

import startup.community.shared.network.model.ApiResult
import startup.community.shared.network.model.map
import startup.community.shared.network.model.response.DetailDiscussionResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DetailDiscussionDomain(
    val id: Int,
    val title: String,
    val description: String,
    val topics: List<TopicDomain>,
    val maker: UserDomain,
    val createdDate: String,
    val replies: Int,
    val comments: List<CommentDomain>,
)

fun Flow<ApiResult<DetailDiscussionResponse>>.toDomain(deviceLang: String?): Flow<ApiResult<DetailDiscussionDomain>> {
    return this.map { response ->
        response.map { data: DetailDiscussionResponse ->
            DetailDiscussionDomain(
                id = data.id,
                title = data.title,
                description = data.description,
                topics = data.topics.map { it.toDomain(deviceLang) },
                maker = data.maker.toDomain(),
                createdDate = data.createdDate,
                replies = data.replies,
                comments = data.comments.map {
                    CommentDomain(
                        id = it.id,
                        text = it.text,
                        user = it.user.toDomain(),
                        createdDate = it.createdDate,
                        isReported = it.isReported
                    )
                }
            )
        }
    }
}