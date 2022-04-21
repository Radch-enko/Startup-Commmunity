package com.multi.producthunt.domain.model

import com.multi.producthunt.BuildKonfig
import com.multi.producthunt.ui.models.StartupUI
import com.multi.producthunt.ui.models.TopicUI

data class HitDomain(
    val id: Int,
    val name: String,
    val tagline: String,
    val thumbnailImageUuid: String?,
    val thumbnailDomain: ThumbnailDomain?,
    val topicDomain: List<TopicDomain>,
    val voteCount: Int
)

fun HitDomain.toStartupUI(): StartupUI {
    return StartupUI(
        this.id.toString(),
        this.name,
        this.tagline,
        if (thumbnailImageUuid.isNullOrEmpty()) {
            thumbnailDomain?.imageUrl
        } else {
            BuildKonfig.IMAGE_SOURCE + this.thumbnailImageUuid
        },
        this.topicDomain.map { topic -> TopicUI(topic.title) }.subList(0, 1),
        null,
        this.voteCount.toString()
    )
}