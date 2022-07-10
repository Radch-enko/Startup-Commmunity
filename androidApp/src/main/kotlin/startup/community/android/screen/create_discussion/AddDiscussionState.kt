package startup.community.android.screen.create_discussion

import startup.community.shared.ui.models.SelectableTopicUI

data class AddDiscussionState(
    val title: String = "",
    val description: String = "",
    var topics: List<SelectableTopicUI> = emptyList(),
    var selectedTopics: List<SelectableTopicUI> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
) {
    fun isFormValid(): Boolean {
        return title.isNotEmpty() && description.isNotEmpty() && selectedTopics.isNotEmpty()
    }

    fun isTopicsValid(): Boolean {
        return selectedTopics.isNotEmpty()
    }
}