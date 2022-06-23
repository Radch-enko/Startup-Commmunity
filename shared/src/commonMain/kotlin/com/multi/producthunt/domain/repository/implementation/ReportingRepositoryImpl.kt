package com.multi.producthunt.domain.repository.implementation

import com.multi.producthunt.domain.repository.ReportingRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.response.SuccessResponse
import com.multi.producthunt.network.service.ProjectsApiService
import kotlinx.coroutines.flow.Flow

class ReportingRepositoryImpl(private val service: ProjectsApiService) : ReportingRepository {

    override fun reportComment(
        commentId: Int,
        isDiscussionComment: Boolean
    ): Flow<ApiResult<SuccessResponse>> {
        return service.reportForComment(commentId, isDiscussionComment)
    }
}