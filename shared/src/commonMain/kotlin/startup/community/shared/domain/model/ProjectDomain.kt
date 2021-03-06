package startup.community.shared.domain.model

import startup.community.shared.network.model.ApiResult
import startup.community.shared.network.model.map
import startup.community.shared.network.model.response.ProjectResponse
import startup.community.shared.network.util.CommonFlow
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

fun Flow<ApiResult<ProjectResponse>>.toDomain(lang: String?): Flow<ApiResult<ProjectDomain>> {
    return this.map { response ->
        response.map { data: ProjectResponse ->
            data.toDomain(lang)
        }
    }
}

fun CommonFlow<ApiResult<List<ProjectResponse>>>.toDomain(lang: String?): Flow<ApiResult<List<ProjectDomain>>> {
    return this.map { apiResult ->
        apiResult.map { listResponse ->
            listResponse.map { projectResponse ->
                projectResponse.toDomain(lang)
            }
        }
    }
}

fun ProjectResponse.toDomain(lang: String?): ProjectDomain {
    return ProjectDomain(
        id = this.id,
        name = this.name,
        tagline = this.tagline,
        thumbnail = this.thumbnail,
        isVoted = this.isVoted,
        topics = this.topics.map {
            it.toDomain(lang)
        },
        votesCount = this.votesCount,
    )
}
