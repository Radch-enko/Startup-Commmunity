package com.multi.producthunt.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailProjectResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("tagline")
    val tagline: String,
    @SerialName("description")
    val description: String,
    @SerialName("thumbnail")
    val thumbnail: String? = null,
    @SerialName("media")
    val media: List<Media?> = emptyList(),
    @SerialName("createdDate")
    val createdDate: String,
    @SerialName("isVoted")
    val isVoted: Boolean,
    @SerialName("topics")
    val topics: List<TopicResponse>,
    @SerialName("votesCount")
    val votesCount: Int,
    @SerialName("ownerLink")
    val ownerLink: String?,
    @SerialName("comments")
    val comments: List<CommentResponse>,
    @SerialName("maker")
    val maker: UserResponse
)