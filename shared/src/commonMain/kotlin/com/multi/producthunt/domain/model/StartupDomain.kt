package com.multi.producthunt.domain.model

import com.multi.producthunt.StartupsQuery
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class StartupDomain(
    val id: String,
    val name: String,
    val tagline: String,
    val thumbnailDomain: StartupThumbnailDomain,
    val commentsCount: Int,
    val topics: List<TopicDomain>,
    val featuredAt: LocalDateTime,
    val votesCount: Int
)

fun StartupsQuery.Node.toDomain(): StartupDomain {
    return StartupDomain(
        id = this.id,
        name = this.name,
        tagline = this.tagline,
        thumbnailDomain = StartupThumbnailDomain(
            url = this.thumbnail?.url
        ),
        commentsCount = this.commentsCount,
        topics = this.topicsInfoResponse.topics.edges.map { topicEdge ->
            TopicDomain(title = topicEdge.node.name)
        }.toList(),
        featuredAt = this.featuredAt.toString().toDate(),
        votesCount = this.votesCount
    )
}

private fun String.toDate(): LocalDateTime {
    return Instant.parse(this).toLocalDateTime(TimeZone.currentSystemDefault())
}
