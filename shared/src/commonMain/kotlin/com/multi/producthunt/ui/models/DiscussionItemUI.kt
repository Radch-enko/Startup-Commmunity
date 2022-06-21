package com.multi.producthunt.ui.models

import com.multi.producthunt.domain.model.DiscussionDomain
import com.multi.producthunt.utils.toFullDate

data class DiscussionItemUI(
    val id: Int,
    val title: String,
    val topics: List<TopicUI>,
    val maker: UiUserCard,
    val createdDate: String?,
    val replies: Int
) {
    companion object {
        val Placeholder =
            DiscussionItemUI(0, "", emptyList(), UiUserCard(0, null, "", null, ""), "", 0)
    }
}

fun DiscussionDomain.toDiscussionItemUI(): DiscussionItemUI {
    return DiscussionItemUI(
        id = this.id,
        title = this.title,
        topics = this.topics.map { TopicUI(it.id, it.title) },
        maker = UiUserCard(
            this.maker.id,
            this.maker.profileImage,
            this.maker.name,
            this.maker.headline,
            this.maker.username
        ),
        createdDate = this.createdDate.toFullDate(),
        replies = this.replies
    )
}