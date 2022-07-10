package startup.community.shared.ui.models

import startup.community.shared.domain.model.DetailDiscussionDomain
import startup.community.shared.utils.toFullDate

data class DetailDiscussionUI(
    val id: Int,
    val title: String,
    val description: String,
    val topics: List<TopicUI>,
    val maker: UiUserCard,
    val createdDate: String?,
    val replies: Int,
    val comments: List<UiComment>,
) {
    companion object {
        val Placeholder =
            DetailDiscussionUI(
                0,
                "",
                "",
                emptyList(),
                UiUserCard(0, null, "", null, ""),
                "",
                0,
                emptyList()
            )
    }
}

fun DetailDiscussionDomain.toUI(): DetailDiscussionUI {
    return DetailDiscussionUI(
        id = this.id,
        title = this.title,
        description = this.description,
        topics = this.topics.map { TopicUI(it.id, it.title) },
        maker = UiUserCard(
            this.maker.id,
            this.maker.profileImage,
            this.maker.name,
            this.maker.headline,
            username = "@${this.maker.username}"
        ),
        createdDate = this.createdDate.toFullDate(),
        replies = this.replies,
        comments = this.comments.map {
            UiComment(
                id = it.id,
                text = it.text,
                user = UiUserCard(
                    id = it.user.id,
                    avatar = it.user.profileImage,
                    name = it.user.name,
                    headline = it.user.headline,
                    username = "@${it.user.username}"
                ),
                createdAt = it.createdDate.toFullDate(),
                childComments = emptyList(),
                isReported = it.isReported
            )
        }
    )
}