package startup.community.shared.ui.models

data class UiComment(
    val id: Int,
    val text: String,
    val createdAt: String?,
    val user: UiUserCard,
    val childComments: List<UiComment>,
    val isReported: Boolean
)