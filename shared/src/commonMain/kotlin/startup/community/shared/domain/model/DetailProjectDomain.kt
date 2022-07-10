package startup.community.shared.domain.model

import startup.community.shared.network.model.ApiResult
import startup.community.shared.network.model.map
import startup.community.shared.network.model.response.DetailProjectResponse
import startup.community.shared.network.model.response.Media
import startup.community.shared.network.util.CommonFlow
import startup.community.shared.utils.getDeviceLang
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
            data.toDomain(getDeviceLang())
        }
    }
}

fun CommonFlow<ApiResult<List<DetailProjectResponse>>>.toDomain(): Flow<ApiResult<List<DetailProjectDomain>>> {
    return this.map { apiResult ->
        apiResult.map { listResponse ->
            listResponse.map { projectResponse ->
                projectResponse.toDomain(getDeviceLang())
            }
        }
    }
}

fun DetailProjectResponse.toDomain(lang: String?): DetailProjectDomain {
    return DetailProjectDomain(
        id = this.id,
        name = this.name,
        tagline = this.tagline,
        description = this.description,
        thumbnail = this.thumbnail,
        media = this.media,
        isVoted = this.isVoted,
        topics = this.topics.map { topicResponse ->
            TopicDomain(
                topicResponse.id,
                if (lang == "ru") topicResponse.nameRu else topicResponse.name,
                topicResponse.image,
                if (lang == "ru") topicResponse.descriptionRu else this.description
            )
        },
        votesCount = this.votesCount,
        ownerLink = this.ownerLink,
        comments = this.comments.map {
            CommentDomain(it.id, it.text, it.user.toDomain(), it.createdDate, it.isReported)
        },
        maker = this.maker.toDomain(),
        createdDate = this.createdDate
    )
}