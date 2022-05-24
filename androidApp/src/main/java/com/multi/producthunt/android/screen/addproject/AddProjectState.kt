package com.multi.producthunt.android.screen.addproject

import android.net.Uri
import com.multi.producthunt.ui.models.SelectableTopicUI

data class AddProjectState(
    val name: String = "",
    val tagline: String = "",
    val description: String = "",
    val thumbnail: Uri? = null,
    val media: List<Uri> = emptyList(),
    var topics: List<SelectableTopicUI> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    fun isFormValid(): Boolean {
        return name.isNotEmpty() && tagline.isNotEmpty()
    }
}