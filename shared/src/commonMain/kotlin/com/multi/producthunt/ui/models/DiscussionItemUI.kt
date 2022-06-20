package com.multi.producthunt.ui.models

import com.multi.producthunt.domain.model.DiscussionDomain

data class DiscussionItemUI(
    val id: Int,
    val title: String,
    val description: String,
    val topics: List<TopicUI>,
    val maker: UiUserCard
) {
    companion object{
        val Placeholder = DiscussionItemUI(0, "", "", emptyList(), UiUserCard(0, null, "", null, ""))
    }
}

fun DiscussionDomain.toDiscussionItemUI(): DiscussionItemUI {
    return DiscussionItemUI(
        id = this.id,
        title = this.title,
        description = this.description,
        topics = this.topics.map { TopicUI(it.id, it.title) },
        maker = UiUserCard(
            this.maker.id,
            this.maker.profileImage,
            this.maker.name,
            this.maker.headline,
            this.maker.username
        )
    )
}