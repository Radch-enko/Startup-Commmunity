package startup.community.shared.ui.models

import startup.community.shared.domain.model.TopicDomain

open class TopicUI(
    open val id: Int,
    open val title: String,
)

class SelectableTopicUI(
    override val id: Int,
    override val title: String,
    var selected: Boolean = false
) : TopicUI(id, title)

fun TopicDomain.toUI(): TopicUI {
    return TopicUI(this.id, this.title)
}

fun TopicDomain.toSelectableUI(): SelectableTopicUI {
    return SelectableTopicUI(this.id, this.title)
}

