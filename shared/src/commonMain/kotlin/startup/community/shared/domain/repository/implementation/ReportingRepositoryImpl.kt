package startup.community.shared.domain.repository.implementation

import kotlinx.coroutines.flow.Flow
import startup.community.shared.domain.repository.ReportingRepository
import startup.community.shared.network.model.ApiResult
import startup.community.shared.network.model.response.SuccessResponse
import startup.community.shared.network.service.ProjectsApiService

class ReportingRepositoryImpl(private val service: ProjectsApiService) : ReportingRepository {

    override fun reportComment(
        commentId: Int,
        isDiscussionComment: Boolean
    ): Flow<ApiResult<SuccessResponse>> {
        return service.reportForComment(commentId, isDiscussionComment)
    }
}