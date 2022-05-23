package com.multi.producthunt.domain.model

import com.multi.producthunt.network.model.response.Media

data class ProjectDomain(
    val id: Int,
    val name: String,
    val tagline: String,
    val thumbnail: String,
    val media: List<Media>,
    val createdDate: String,
    val isVoted: Boolean,
    val topics: List<TopicDomain>,
    val voters: List<UserDomain>,
    val votesCount: Int
)

