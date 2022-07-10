package startup.community.shared.domain.repository

import kotlinx.coroutines.flow.Flow
import startup.community.shared.domain.model.DetailDiscussionDomain
import startup.community.shared.domain.model.DiscussionDomain
import startup.community.shared.network.model.ApiResult

interface DiscussionsRepository {

    fun getDiscussions(
        cursor: Int,
        pageSize: Int?,
        searchQuery: String?
    ): Flow<ApiResult<List<DiscussionDomain>>>

    fun getDetailDiscussion(
        id: Int
    ): Flow<ApiResult<DetailDiscussionDomain>>

    fun createDiscussion(
        title: String,
        description: String,
        topics: List<Int>,
    ): Flow<ApiResult<DiscussionDomain>>

    fun commentForDiscussion(
        discussionId: Int,
        text: String
    ): Flow<ApiResult<DetailDiscussionDomain>>

}