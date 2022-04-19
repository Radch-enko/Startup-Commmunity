package com.multi.producthunt.ui.models

import com.multi.producthunt.domain.model.StartupDomain

class StartupUI(
    val id: String,
    val name: String,
    val tagline: String,
    val url: String?,
    val topics: List<TopicUI>,
    val featuredAt: String,
    val votesCount: String
) {
    companion object {
        val Placeholder = StartupUI(
            id = "placeholder",
            name = "placeholder",
            tagline = "placeholder",
            url = "placeholder",
            topics = listOf(
                TopicUI("Placeholder"),
                TopicUI("Placeholder"),
                TopicUI("Placeholder")
            ),
            featuredAt = "placeholder",
            votesCount = "123"
        )
    }
}

fun StartupDomain.toUI(): StartupUI {
    val date = this.featuredAt
    return StartupUI(
        id,
        name,
        tagline,
        thumbnailDomain.url,
        this.topics.map { topicDomain -> TopicUI(topicDomain.title) },
        featuredAt = "${date.month.name.uppercaseFirstSymbol()} ${date.dayOfMonth}th",
        votesCount = this.votesCount.toString()
    )
}

private fun String.uppercaseFirstSymbol(): String {
    return this[0] + this.substring(1).lowercase()
}
