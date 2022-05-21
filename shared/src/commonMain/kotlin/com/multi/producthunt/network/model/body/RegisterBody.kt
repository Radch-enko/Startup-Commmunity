package com.multi.producthunt.network.model.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class RegisterBody(
    @SerialName("name")
    val name: String,
    @SerialName("username")
    val username: String,
    @SerialName("headline")
    val headline: String,
    @SerialName("profileImage")
    val profileImage: String,
    @SerialName("coverImage")
    val coverImage: String,
    @SerialName("password")
    val password: String,
    @SerialName("password2")
    val password2: String,
)