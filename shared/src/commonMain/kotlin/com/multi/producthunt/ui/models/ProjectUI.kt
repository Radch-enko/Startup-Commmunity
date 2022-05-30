package com.multi.producthunt.ui.models

import com.multi.producthunt.domain.model.ProjectDomain

class ProjectUI(
    val id: Int,
    val name: String,
    val tagline: String,
    val url: String?,
    val topics: List<TopicUI>,
    val votesCount: Int,
    val isVoted: Boolean = false
) {
    companion object {
        val Placeholder = ProjectUI(
            id = 0,
            name = "placeholder",
            tagline = "placeholder",
            url = "placeholder",
            topics = listOf(
                TopicUI(0, "Placeholder"),
                TopicUI(0, "Placeholder"),
            ),
            votesCount = 0,
            false
        )
    }
}

fun ProjectDomain.toUI(): ProjectUI {
    return ProjectUI(
        id,
        name,
        tagline,
        thumbnail,
        this.topics.map { topicDomain -> TopicUI(topicDomain.id, topicDomain.title) }.take(2),
        votesCount = this.votesCount,
        isVoted = isVoted
    )
}