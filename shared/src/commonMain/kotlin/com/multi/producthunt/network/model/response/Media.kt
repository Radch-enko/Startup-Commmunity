package com.multi.producthunt.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class Media(
    val type: String,
    val url: String
)