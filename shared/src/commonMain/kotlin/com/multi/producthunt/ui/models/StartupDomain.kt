package com.multi.producthunt.ui.models

import com.multi.producthunt.domain.model.StartupDomain
import com.multi.producthunt.domain.model.StartupThumbnailDomain

class StartupUI(
    val id: String,
    val name: String,
    val tagline: String,
    val thumbnailDomain: StartupThumbnailDomain
)

fun StartupDomain.toUI(): StartupUI {
    return StartupUI(id, name, tagline, thumbnailDomain)
}