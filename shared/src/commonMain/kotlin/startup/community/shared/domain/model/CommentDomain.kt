package startup.community.shared.domain.model

data class CommentDomain(
    val id: Int,
    val text: String,
    val user: UserDomain,
    val createdDate: String,
    val isReported: Boolean
)
