package startup.community.shared.ui.models

import startup.community.shared.domain.model.TopicDomain

data class DetailTopicUI(
    val id: Int,
    val title: String,
    val image: String,
    val description: String
)

fun TopicDomain.toDetailTopicUI(): DetailTopicUI {
    return DetailTopicUI(id, title, image, description)
}