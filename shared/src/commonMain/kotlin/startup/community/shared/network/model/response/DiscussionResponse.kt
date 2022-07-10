package startup.community.shared.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscussionResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("topics")
    val topics: List<TopicResponse>,
    @SerialName("maker")
    val maker: UserResponse,
    @SerialName("createdDate")
    val createdDate: String,
    @SerialName("replies")
    val replies: Int
)