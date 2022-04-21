package com.multi.producthunt.domain.model

data class SearchableStartupsDomain(
    val startups: List<HitDomain>,
    val pages: Int,
    val currentPage: Int,
)