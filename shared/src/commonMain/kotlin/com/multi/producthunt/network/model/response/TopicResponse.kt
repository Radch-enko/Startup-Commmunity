package com.multi.producthunt.network.model.response

import kotlinx.serialization.Serializable

@Serializable
class TopicResponse(
    val id: Int,
    val name: String
)