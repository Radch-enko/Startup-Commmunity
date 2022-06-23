package com.multi.producthunt.ui.models

import com.multi.producthunt.domain.model.DetailProjectDomain
import com.multi.producthunt.utils.toCommentDate
import com.multi.producthunt.utils.toFullDate

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
    val comments: List<UiComment>,
    val maker: UiUserCard,
    val createdDate: String?,
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
            emptyList(),
            UiUserCard(0, "", "", "", ""),
            ""
        )
    }
}

fun DetailProjectDomain.toDetailUI(): DetailProjectUI {
    return DetailProjectUI(
        id,
        name,
        tagline,
        description,
        thumbnail,
        media.mapNotNull { it?.url },
        isVoted = isVoted,
        topics.map { TopicUI(it.id, it.title) },
        votesCount = votesCount,
        ownerLink,
        comments.map {
            UiComment(
                id = it.id,
                text = it.text,
                createdAt = it.createdDate.toCommentDate(),
                user = UiUserCard(
                    it.user.id,
                    avatar = it.user.profileImage,
                    name = it.user.name,
                    headline = it.user.headline,
                    username = "@${it.user.username}"
                ),
                childComments = emptyList(),
                isReported = it.isReported
            )
        },
        maker = UiUserCard(
            id = maker.id,
            name = maker.name,
            headline = maker.headline,
            avatar = maker.profileImage,
            username = "@${maker.username}"
        ),
        createdDate = createdDate.toFullDate()
    )
}
