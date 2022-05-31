package com.multi.producthunt.ui.models

data class UiComment(
    val text: String,
    val createdAt: String?,
    val user: UiUserCard,
    val childComments: List<UiComment>
)