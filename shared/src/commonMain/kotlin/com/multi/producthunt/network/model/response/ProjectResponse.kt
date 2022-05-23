package com.multi.producthunt.network.model.response

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
    val thumbnail: String,
    @SerialName("media")
    val media: List<Media>,
    @SerialName("createdDate")
    val createdDate: String,
    @SerialName("isVoted")
    val isVoted: Boolean,
    @SerialName("topics")
    val topics: List<Topic>,
    @SerialName("voters")
    val voters: List<UserResponse>,
    @SerialName("votesCount")
    val votesCount: Int
)