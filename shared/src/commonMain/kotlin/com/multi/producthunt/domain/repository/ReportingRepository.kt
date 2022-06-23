package com.multi.producthunt.domain.repository

import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.network.model.response.SuccessResponse
import kotlinx.coroutines.flow.Flow

interface ReportingRepository {

    fun reportComment(
        commentId: Int,
        isDiscussionComment: Boolean = false
    ): Flow<ApiResult<SuccessResponse>>
}