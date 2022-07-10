package startup.community.android.screen.addproject

import android.net.Uri
import startup.community.shared.ui.models.SelectableTopicUI

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
                && selectedTopics.isNotEmpty()
    }

    fun isTopicsValid(): Boolean {
        return selectedTopics.isNotEmpty()
    }
}