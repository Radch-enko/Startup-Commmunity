package startup.community.shared.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SuccessResponse(
    val success: String
)