package com.multi.producthunt.ui.models

import com.multi.producthunt.domain.model.DetailProjectDomain
import com.multi.producthunt.domain.model.ProjectDomain
import io.github.aakira.napier.Napier
import kotlinx.datetime.toLocalDateTime

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
    val makerId: Int
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
            0
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
                it.text,
                createdAt = it.createdDate.toCommentDate(),
                user = UiUserCard(
                    it.user.id,
                    avatar = it.user.profileImage,
                    name = it.user.name,
                    headline = it.user.headline
                ),
                childComments = emptyList()
            )
        },
        makerId = makerId
    )
}

private fun String.toCommentDate(): String? {
    return try {
        val localDateTime = this.substringBefore(".").toLocalDateTime()

        "${localDateTime.dayOfMonth} ${localDateTime.month.name[0]}${
            localDateTime.month.name.substring(
                1
            ).lowercase()
        }"
    } catch (e: Exception) {
        Napier.e("CommentDateCast", e)
        null
    }
}
