package com.multi.producthunt.network.model.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CreateDiscussionBody(
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("topics")
    val topics: List<TopicBody>,
)
