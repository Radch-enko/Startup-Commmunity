package com.multi.producthunt.android.screen.create_discussion

import android.net.Uri
import com.multi.producthunt.ui.models.SelectableTopicUI

data class AddProjectState(
    val name: String = "",
    val tagline: String = "",
    val description: String = "",
    val ownerLink: String = "",
    val thumbnail: Uri? = null,
    val media: List<Uri> = emptyList(),
    var topics: List<SelectableTopicUI> = emptyList(),
    var selectedTopics: List<SelectableTopicUI> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val isRedact: Boolean = false,
) {
    fun isFormValid(): Boolean {
        return name.isNotEmpty() && tagline.isNotEmpty() && description.isNotEmpty() && ownerLink.isNotEmpty() && thumbnail != null && media.isNotEmpty()
                && topics.isNotEmpty()
    }

    fun isTopicsValid(): Boolean {
        return selectedTopics.isNotEmpty()
    }
}