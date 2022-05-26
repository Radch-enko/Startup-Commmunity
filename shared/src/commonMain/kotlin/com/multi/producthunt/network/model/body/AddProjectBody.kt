package com.multi.producthunt.network.model.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddProjectBody(
    @SerialName("name")
    val name: String,
    @SerialName("tagline")
    val tagline: String,
    @SerialName("description")
    val description: String,
    @SerialName("thumbnail")
    val thumbnail: String? = null,
    @SerialName("media")
    val media: List<String> = emptyList(),
    @SerialName("topics")
    val topics: List<TopicBody> = emptyList()
)