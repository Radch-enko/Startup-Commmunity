package com.multi.producthunt.ui.models

import com.multi.producthunt.domain.model.UserDomain

data class SearchUserUI(
    val id: Int,
    val username: String,
    val name: String,
    val profileImage: String?
) {
    companion object {
        val Placeholder = SearchUserUI(
            id = 0,
            username = "",
            name = "",
            profileImage = null
        )
    }
}

fun UserDomain.toSearchUserUI(): SearchUserUI {
    return SearchUserUI(
        id = id,
        username = username,
        name = name,
        profileImage = profileImage
    )
}