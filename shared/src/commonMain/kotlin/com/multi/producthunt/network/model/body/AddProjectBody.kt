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
    val thumbnail: ByteArray? = null,
    @SerialName("media")
    val media: List<ByteArray> = emptyList(),
    @SerialName("topics")
    val topics: List<TopicBody> = emptyList()
)