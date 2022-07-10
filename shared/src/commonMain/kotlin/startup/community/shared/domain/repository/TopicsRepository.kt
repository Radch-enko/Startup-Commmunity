package startup.community.shared.domain.repository

import kotlinx.coroutines.flow.Flow
import startup.community.shared.domain.model.TopicDomain
import startup.community.shared.network.model.ApiResult

interface TopicsRepository {
    fun getTopics(): Flow<ApiResult<List<TopicDomain>>>
    fun getDiscussionTopics(): Flow<ApiResult<List<TopicDomain>>>
}