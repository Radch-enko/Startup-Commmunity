package startup.community.shared.domain.repository

import startup.community.shared.network.model.ApiResult
import startup.community.shared.network.model.response.SuccessResponse
import kotlinx.coroutines.flow.Flow

interface ReportingRepository {

    fun reportComment(
        commentId: Int,
        isDiscussionComment: Boolean = false
    ): Flow<ApiResult<SuccessResponse>>
}