package startup.community.shared.domain.repository.implementation

import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import startup.community.shared.domain.model.DetailProjectDomain
import startup.community.shared.domain.model.ProjectDomain
import startup.community.shared.domain.model.toDomain
import startup.community.shared.domain.repository.StartupsRepository
import startup.community.shared.network.model.ApiResult
import startup.community.shared.network.model.body.AddProjectBody
import startup.community.shared.network.model.body.CreateCommentBody
import startup.community.shared.network.model.body.TopicBody
import startup.community.shared.network.model.response.SuccessResponse
import startup.community.shared.network.service.ProjectsApiService
import startup.community.shared.network.util.asCommonFlow
import startup.community.shared.utils.getDeviceLang

class StartupsRepositoryImpl(
    private val service: ProjectsApiService
) :
    StartupsRepository {

    override fun addProject(
        name: String,
        tagline: String,
        description: String,
        ownerLink: String,
        thumbnail: String?,
        media: List<String?>,
        topics: List<Int>
    ): Flow<ApiResult<ProjectDomain>> {
        return service.addProject(AddProjectBody(
            name = name,
            tagline = tagline,
            description = description,
            ownerLink = ownerLink,
            thumbnail = thumbnail,
            media = media.filterNotNull(),
            topics = topics.map { TopicBody(it) }
        )).toDomain(getDeviceLang())
    }

    override fun updateProject(
        projectId: Int,
        name: String,
        tagline: String,
        description: String,
        ownerLink: String,
        thumbnail: String?,
        media: List<String?>,
        topics: List<Int>
    ): Flow<ApiResult<ProjectDomain>> {
        Napier.e("$media")
        return service.updateProject(projectId, AddProjectBody(
            name = name,
            tagline = tagline,
            description = description,
            ownerLink = ownerLink,
            thumbnail = thumbnail,
            media = media.filterNotNull(),
            topics = topics.map { TopicBody(it) }
        )).toDomain(getDeviceLang())
    }

    override fun deleteProject(projectId: Int): Flow<ApiResult<SuccessResponse>> {
        return service.deleteProject(projectId)
    }

    override fun getProjects(
        cursor: Int,
        pageSize: Int?,
        day: String?,
        makerId: Int?,
        topicId: Int?,
        searchQuery: String?
    ): Flow<ApiResult<List<ProjectDomain>>> {
        return if (makerId != null) {
            service.getMakerProjects(
                cursor = cursor,
                pageSize = pageSize ?: 10,
                makerId = makerId
            ).asCommonFlow()
                .toDomain(getDeviceLang())
        } else if (day != null) {
            service.getProjectsByDay(
                cursor = cursor,
                pageSize = pageSize ?: 10,
                day = day
            ).asCommonFlow()
                .toDomain(getDeviceLang())
        } else if (topicId != null) {
            service.getProjectsByTopicId(
                cursor = cursor,
                pageSize = pageSize ?: 10,
                topicId = topicId
            ).asCommonFlow()
                .toDomain(getDeviceLang())
        } else if (!searchQuery.isNullOrEmpty()) {
            service.searchProjects(
                cursor = cursor,
                pageSize = pageSize ?: 10,
                mapOf("searchQuery" to searchQuery)
            ).asCommonFlow()
                .toDomain(getDeviceLang())
        } else {
            service.getProjects(
                cursor = cursor,
                pageSize = pageSize ?: 10
            ).asCommonFlow()
                .toDomain(getDeviceLang())
        }
    }

    override fun getProjectById(projectId: Int): Flow<ApiResult<DetailProjectDomain>> {
        return service.getProjectById(projectId).toDomain()
    }

    override fun commentForProject(
        projectId: Int,
        text: String
    ): Flow<ApiResult<DetailProjectDomain>> {
        return service.commentForProject(
            CreateCommentBody(projectId, text)
        ).toDomain()
    }

    override fun voteProject(projectId: Int): Flow<ApiResult<SuccessResponse>> {
        return service.voteForProject(projectId)
    }
}





