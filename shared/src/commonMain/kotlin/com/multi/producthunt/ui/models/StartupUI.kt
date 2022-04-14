package com.multi.producthunt.ui.models

import com.multi.producthunt.domain.model.StartupDomain

class StartupUI(
    val id: String,
    val name: String,
    val tagline: String,
    val url: String?,
    val commentsCount: Int,
    val topics: List<TopicUI>
)

fun StartupDomain.toUI(): StartupUI {
    return StartupUI(
        id,
        name,
        tagline,
        thumbnailDomain.url,
        this.commentsCount,
        this.topics.map { topicDomain -> TopicUI(topicDomain.title) })
}