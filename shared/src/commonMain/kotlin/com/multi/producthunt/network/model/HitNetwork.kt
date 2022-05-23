package com.multi.producthunt.network.model

import com.multi.producthunt.domain.model.HitDomain
import com.multi.producthunt.domain.model.ThumbnailDomain
import com.multi.producthunt.domain.model.TopicDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HitNetwork(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("tagline")
    val tagline: String,
    @SerialName("thumbnail")
    val thumbnailNetwork: ThumbnailNetwork?,
    @SerialName("thumbnail_image_uuid")
    val thumbnailImageUuid: String?,
    @SerialName("topics")
    val topicNetworks: List<TopicNetwork>,
    @SerialName("vote_count")
    val voteCount: Int
)

fun HitNetwork.toDomain(): HitDomain {
    return HitDomain(
        this.id,
        this.name,
        this.tagline,
        this.thumbnailImageUuid,
        ThumbnailDomain(this.thumbnailNetwork?.imageUrl),
        this.topicNetworks.map { TopicDomain(it.id, it.name) },
        this.voteCount
    )
}