package startup.community.shared.network.model.body

import kotlinx.serialization.Serializable

@Serializable
data class TopicBody(
    val topicId: Int
)