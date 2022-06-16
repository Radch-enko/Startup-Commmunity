package com.multi.producthunt.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SuccessResponse(
    val success: String
)