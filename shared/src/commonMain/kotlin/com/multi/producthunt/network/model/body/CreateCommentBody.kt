package com.multi.producthunt.network.model.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateCommentBody(
    @SerialName("project_id")
    val projectId: Int,
    @SerialName("text")
    val text: String
)