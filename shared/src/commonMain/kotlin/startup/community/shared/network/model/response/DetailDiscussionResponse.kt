package startup.community.shared.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailDiscussionResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("topics")
    val topics: List<TopicResponse>,
    @SerialName("maker")
    val maker: UserResponse,
    @SerialName("createdDate")
    val createdDate: String,
    @SerialName("replies")
    val replies: Int,
    @SerialName("comments")
    val comments: List<CommentResponse>,
)