package startup.community.shared.domain.repository.implementation

import kotlinx.coroutines.flow.Flow
import startup.community.shared.domain.model.DetailDiscussionDomain
import startup.community.shared.domain.model.DiscussionDomain
import startup.community.shared.domain.model.toDomain
import startup.community.shared.domain.repository.DiscussionsRepository
import startup.community.shared.network.model.ApiResult
import startup.community.shared.network.model.body.CreateDiscussionBody
import startup.community.shared.network.model.body.CreateDiscussionCommentBody
import startup.community.shared.network.model.body.TopicBody
import startup.community.shared.network.service.ProjectsApiService
import startup.community.shared.network.util.asCommonFlow
import startup.community.shared.utils.getDeviceLang

class DiscussionRepositoryImpl(
    private val service: ProjectsApiService
) : DiscussionsRepository {

    override fun getDiscussions(
        cursor: Int,
        pageSize: Int?,
        searchQuery: String?
    ): Flow<ApiResult<List<DiscussionDomain>>> {
        return service.getDiscussions(
            cursor,
            pageSize ?: 10,
            if (searchQuery.isNullOrEmpty()) emptyMap() else mapOf("searchQuery" to searchQuery)
        )
            .asCommonFlow()
            .toDomain(getDeviceLang())
    }

    override fun getDetailDiscussion(id: Int): Flow<ApiResult<DetailDiscussionDomain>> {
        return service.getDiscussionById(id).toDomain(getDeviceLang())
    }

    override fun createDiscussion(
        title: String,
        description: String,
        topics: List<Int>
    ): Flow<ApiResult<DiscussionDomain>> {
        return service.createDiscussion(
            body = CreateDiscussionBody(
                title,
                description,
                topics.map { TopicBody(it) }
            )
        ).toDomain(getDeviceLang())
    }

    override fun commentForDiscussion(
        discussionId: Int,
        text: String
    ): Flow<ApiResult<DetailDiscussionDomain>> {
        return service.commentForDiscussion(CreateDiscussionCommentBody(discussionId, text))
            .toDomain(
                getDeviceLang()
            )
    }
}


