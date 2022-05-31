package com.multi.producthunt.domain.model

data class CommentDomain(
    val text: String,
    val user: UserDomain,
    val createdDate: String
)
