package com.multi.producthunt.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ThumbnailNetwork(
    @SerialName("image_url")
    val imageUrl: String,
)