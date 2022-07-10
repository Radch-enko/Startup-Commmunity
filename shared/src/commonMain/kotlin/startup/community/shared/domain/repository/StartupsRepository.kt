package startup.community.shared.domain.repository

import kotlinx.coroutines.flow.Flow
import startup.community.shared.domain.model.DetailProjectDomain
import startup.community.shared.domain.model.ProjectDomain
import startup.community.shared.network.model.ApiResult
import startup.community.shared.network.model.response.SuccessResponse

interface StartupsRepository {
    fun addProject(
        name: String,
        tagline: String,
        description: String,
        ownerLink: String,
        thumbnail: String? = null,
        media: List<String?> = emptyList(),
        topics: List<Int>
    ): Flow<ApiResult<ProjectDomain>>

    fun updateProject(
        projectId: Int,
        name: String,
        tagline: String,
        description: String,
        ownerLink: String,
        thumbnail: String? = null,
        media: List<String?> = emptyList(),
        topics: List<Int>
    ): Flow<ApiResult<ProjectDomain>>

    fun deleteProject(
        projectId: Int,
    ): Flow<ApiResult<SuccessResponse>>

    fun getProjects(
        cursor: Int,
        pageSize: Int?,
        day: String?,
        makerId: Int?,
        topicId: Int?,
        searchQuery: String?
    ): Flow<ApiResult<List<ProjectDomain>>>

    fun getProjectById(projectId: Int): Flow<ApiResult<DetailProjectDomain>>

    fun commentForProject(projectId: Int, text: String): Flow<ApiResult<DetailProjectDomain>>

    fun voteProject(
        projectId: Int
    ): Flow<ApiResult<SuccessResponse>>
}