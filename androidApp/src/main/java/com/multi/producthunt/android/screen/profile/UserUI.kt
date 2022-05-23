package com.multi.producthunt.android.screen.profile

import com.multi.producthunt.domain.model.UserDomain

data class UserUI(
    val name: String,
    val username: String,
    val headline: String? = null,
    val profileImage: String? = null,
    val coverImage: String? = null,
)

fun UserDomain.toUI(): UserUI {
    return UserUI(
        name, "@$username", headline, profileImage, coverImage
    )
}
