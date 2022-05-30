package com.multi.producthunt.ui.models

import com.multi.producthunt.domain.model.ProjectDomain

data class DetailProjectUI(
    val id: Int,
    val name: String,
    val tagline: String,
    val description: String,
    val thumbnail: String?,
    val media: List<String>,
    val isVoted: Boolean,
    val topics: List<TopicUI>,
    val votesCount: Int,
    val ownerLink: String?,
    val comments: List<UiComment>
) {
    companion object {
        val Empty = DetailProjectUI(
            0,
            "",
            "",
            "",
            "",
            emptyList(),
            false,
            emptyList(),
            0,
            null,
            emptyList()
        )
    }
}

fun ProjectDomain.toDetailUI(): DetailProjectUI {
    return DetailProjectUI(
        id,
        name,
        tagline,
        description,
        thumbnail,
        media.mapNotNull { it?.url },
        isVoted,
        topics.map { TopicUI(it.id, it.title) },
        votesCount = votesCount,
        ownerLink,
        comments.map {
            UiComment(
                it.text,
                createdAt = it.createdDate,
                user = UiUserCard(
                    it.user.id,
                    avatar = it.user.profileImage,
                    name = it.user.name,
                    headline = it.user.headline,
                    isMaker = false
                ),
                childComments = emptyList()
            )
        }
    )
}