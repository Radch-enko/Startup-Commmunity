package com.multi.producthunt.android.screen.addproject

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.multi.producthunt.domain.repository.StartupsRepository
import com.multi.producthunt.domain.repository.TopicsRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.ui.models.SelectableTopicUI
import com.multi.producthunt.ui.models.toSelectableUI
import com.multi.producthunt.utils.KMMPreference
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddProjectViewModel(
    private val startupsRepository: StartupsRepository,
    private val topicsRepository: TopicsRepository,
    private val kmmPreference: KMMPreference
) :
    StateScreenModel<AddProjectState>(
        AddProjectState()
    ) {

    sealed class Event {

        class NameChanged(val name: String) :
            Event()

        class TaglineChanged(val tagline: String) :
            Event()

        class ThumnailChanged(val thumbnail: ByteArray) :
            Event()

        class DescriptionChanged(val description: String) :
            Event()

        class MediaChanged(val mediaList: List<ByteArray>) :
            Event()

        class TopicChanged(val topic: SelectableTopicUI) :
            Event()

        object AddProject : Event()

        object ErrorDismissed : Event()
    }

    sealed class Effect {
        object Success : Effect()
    }

    private val mutableEffect = MutableSharedFlow<Effect>()
    val effect = mutableEffect.asSharedFlow()

    init {
        loadTopics()
    }

    private fun loadTopics() = coroutineScope.launch {
        topicsRepository.getTopics().collectLatest { response ->
            when (response) {
                is ApiResult.Error -> {
                    mutableState.update {
                        it.copy(error = response.exception)
                    }
                }
                is ApiResult.Success -> {
                    mutableState.update {
                        it.copy(topics = response._data.map { it.toSelectableUI() })
                    }
                }
            }
        }
    }

    fun sendEvent(event: Event) {
        when (event) {
            Event.AddProject -> addProject()
            Event.ErrorDismissed -> dismissError()
            is Event.MediaChanged -> updatedMedia(event.mediaList)
            is Event.NameChanged -> updateName(event.name)
            is Event.TaglineChanged -> updateTagline(event.tagline)
            is Event.ThumnailChanged -> updateThumbnail(event.thumbnail)
            is Event.TopicChanged -> updateTopics(event.topic)
            is Event.DescriptionChanged -> updateDescription(event.description)
        }
    }

    private fun updateDescription(description: String) {
        mutableState.update {
            it.copy(description = description)
        }
    }

    private fun addProject() = coroutineScope.launch {
        startupsRepository.addProject(
            kmmPreference.getString("ACCESS_TOKEN"),
            name = state.value.name,
            tagline = state.value.tagline,
            thumbnail = state.value.thumbnail,
            description = state.value.description,
            media = state.value.media,
            topics = state.value.topics.map {
                it.id
            }
        )
    }

    private fun updateTopics(topic: SelectableTopicUI) {
        mutableState.update {
            it.copy(topics = it.topics.map { selectableTopic ->
                if (selectableTopic.id == topic.id) {
                    selectableTopic.selected = !topic.selected
                }
                selectableTopic
            })
        }
    }

    private fun updateThumbnail(thumbnail: ByteArray) {
        mutableState.update {
            it.copy(thumbnail = thumbnail)
        }
    }

    private fun updateTagline(tagline: String) {
        mutableState.update {
            it.copy(tagline = tagline)
        }
    }

    private fun updateName(name: String) {
        mutableState.update {
            it.copy(name = name)
        }
    }

    private fun updatedMedia(list: List<ByteArray>) {
        mutableState.update {
            it.copy(media = list)
        }
    }

    private fun dismissError() {
        mutableState.update {
            it.copy(error = null)
        }
    }
}