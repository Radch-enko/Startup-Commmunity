package startup.community.shared.network.model.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateDiscussionCommentBody(
    @SerialName("discussion_id")
    val discussionId: Int,
    @SerialName("text")
    val text: String
)