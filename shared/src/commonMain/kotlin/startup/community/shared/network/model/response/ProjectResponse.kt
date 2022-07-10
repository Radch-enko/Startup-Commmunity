package startup.community.shared.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("tagline")
    val tagline: String,
    @SerialName("thumbnail")
    val thumbnail: String? = null,
    @SerialName("isVoted")
    val isVoted: Boolean,
    @SerialName("topics")
    val topics: List<TopicResponse>,
    @SerialName("votesCount")
    val votesCount: Int
)