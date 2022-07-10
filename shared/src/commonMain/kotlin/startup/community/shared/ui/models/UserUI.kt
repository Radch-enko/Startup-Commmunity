package startup.community.shared.ui.models

import startup.community.shared.domain.model.UserDomain

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
