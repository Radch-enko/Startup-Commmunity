package com.multi.producthunt.domain.model

import com.multi.producthunt.StartupsQuery

class PostsDomain(val list: List<StartupDomain>, val start: String?, val end: String?)

fun StartupsQuery.Posts.toDomain(): PostsDomain {
    return PostsDomain(
        list = this.edges.map { edge -> edge.node.toDomain() },
        this.pageInfoResponse.pageInfo.startCursor,
        this.pageInfoResponse.pageInfo.endCursor
    )
}