package com.multi.producthunt.domain.model

import com.multi.producthunt.StartupsQuery

data class StartupDomain(
    val id: String,
    val name: String,
    val tagline: String,
    val thumbnailDomain: StartupThumbnailDomain,
    val commentsCount: Int,
    val topics: List<TopicDomain>
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
        }.toList()
    )
}