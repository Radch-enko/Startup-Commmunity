package startup.community.shared.domain.repository.implementation

import kotlinx.coroutines.flow.Flow
import startup.community.shared.domain.model.TopicDomain
import startup.community.shared.domain.model.toDomain
import startup.community.shared.domain.repository.TopicsRepository
import startup.community.shared.network.model.ApiResult
import startup.community.shared.network.service.ProjectsApiService
import startup.community.shared.utils.getDeviceLang

class TopicsRepositoryImpl(private val service: ProjectsApiService) : TopicsRepository {
    override fun getTopics(): Flow<ApiResult<List<TopicDomain>>> {
        return service.getTopics().toDomain(getDeviceLang())
    }

    override fun getDiscussionTopics(): Flow<ApiResult<List<TopicDomain>>> {
        return service.getDiscussionsTopics().toDomain(getDeviceLang())
    }
}