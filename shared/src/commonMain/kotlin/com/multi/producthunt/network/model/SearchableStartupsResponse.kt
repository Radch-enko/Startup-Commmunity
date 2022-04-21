package com.multi.producthunt.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchableStartupsResponse(
    @SerialName("hits")
    val startups: List<HitNetwork>,
    @SerialName("nbPages")
    val pages: Int,
    @SerialName("page")
    val currentPage: Int,
)