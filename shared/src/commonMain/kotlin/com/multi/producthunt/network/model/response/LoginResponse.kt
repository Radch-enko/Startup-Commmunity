package com.multi.producthunt.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LoginResponse(
    @SerialName("access_token")
    val token: String,
    @SerialName("token_type")
    val type: String
)