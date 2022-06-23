package com.multi.producthunt.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("text")
    val text: String,
    @SerialName("user")
    val user: UserResponse,
    @SerialName("createdDate")
    val createdDate: String,
    @SerialName("isReported")
    val isReported: Boolean
)