package com.multi.producthunt.android.screen.addproject

import com.multi.producthunt.ui.models.SelectableTopicUI

data class AddProjectState(
    val name: String = "",
    val tagline: String = "",
    val description: String = "",
    val thumbnail: ByteArray? = null,
    val media: List<ByteArray> = emptyList(),
    val topics: List<SelectableTopicUI> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    fun isFormValid(): Boolean {
        return name.isNotEmpty() && tagline.isNotEmpty()
    }
}