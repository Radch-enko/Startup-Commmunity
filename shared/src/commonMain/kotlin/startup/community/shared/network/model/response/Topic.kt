package startup.community.shared.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class Topic(
    val id: Int,
    val name: String
)