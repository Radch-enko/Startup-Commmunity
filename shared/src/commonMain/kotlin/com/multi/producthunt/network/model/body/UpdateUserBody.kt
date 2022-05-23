package com.multi.producthunt.network.model.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserBody(
    @SerialName("name")
    val name: String? = null,
    @SerialName("headline")
    val headline: String? = null
)
