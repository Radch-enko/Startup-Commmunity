package com.multi.producthunt.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicNetwork(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)