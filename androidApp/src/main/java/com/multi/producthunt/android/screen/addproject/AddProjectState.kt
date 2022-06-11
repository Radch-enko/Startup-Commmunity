package com.multi.producthunt.android.screen.addproject

import android.net.Uri
import android.webkit.URLUtil
import com.multi.producthunt.ui.models.SelectableTopicUI

data class AddProjectState(
    val name: String = "",
    val tagline: String = "",
    val description: String = "",
    val ownerLink: String = "",
    val thumbnail: Uri? = null,
    val media: List<Uri> = emptyList(),
    var topics: List<SelectableTopicUI> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
) {
    fun isFormValid(): Boolean {
        return name.isNotEmpty() && tagline.isNotEmpty() && description.isNotEmpty() && ownerLink.isNotEmpty() && ownerLink.isValidUrl() && thumbnail != null && media.isNotEmpty()
                && topics.isNotEmpty()
    }

    fun String.isValidUrl(): Boolean {
        return URLUtil.isValidUrl(this)
    }
}