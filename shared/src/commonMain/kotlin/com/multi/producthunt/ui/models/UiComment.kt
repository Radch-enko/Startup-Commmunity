package com.multi.producthunt.ui.models

data class UiComment(
    val id: Int,
    val text: String,
    val createdAt: String?,
    val user: UiUserCard,
    val childComments: List<UiComment>,
    val isReported: Boolean
)