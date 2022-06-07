package com.multi.producthunt.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TopicResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("image")
    val image: String,
    @SerialName("description")
    val description: String,
)