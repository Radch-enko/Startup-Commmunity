package com.multi.producthunt.android.screen.addproject

import android.content.Context
import android.net.Uri
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.multi.producthunt.android.ui.toBase64
import com.multi.producthunt.android.ui.toByteArray
import com.multi.producthunt.domain.repository.StartupsRepository
import com.multi.producthunt.domain.repository.TopicsRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.ui.models.SelectableTopicUI
import com.multi.producthunt.ui.models.toSelectableUI
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddProjectViewModel(
    private val startupsRepository: StartupsRepository,
    private val topicsRepository: TopicsRepository,
    private val context: Context
) :
    StateScreenModel<AddProjectState>(
        AddProjectState()
    ) {

    sealed class Event {

        class NameChanged(val name: String) :
            Event()

        class TaglineChanged(val tagline: String) :
            Event()

        class ThumnailChanged(val thumbnail: Uri?) :
            Event()

        class DescriptionChanged(val description: String) :
            Event()

        class MediaChanged(val media: Uri) :
            Event()

        class MediaDeleted(val position: Int) :
            Event()

        class TopicChanged(val topic: SelectableTopicUI) :
            Event()

        class OwnerLinkChanged(val ownerLink: String) :
            Event()


        object AddProject : Event()

        object ErrorDismissed : Event()
    }

    sealed class Effect {
        class Success(val projectId: Int) : Effect()
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
            is Event.MediaChanged -> updatedMedia(event.media)
            is Event.NameChanged -> updateName(event.name)
            is Event.TaglineChanged -> updateTagline(event.tagline)
            is Event.ThumnailChanged -> updateThumbnail(event.thumbnail)
            is Event.TopicChanged -> updateTopics(event.topic)
            is Event.DescriptionChanged -> updateDescription(event.description)
            is Event.OwnerLinkChanged -> updateOwnerlink(event.ownerLink)
            is Event.MediaDeleted -> deleteMedia(event.position)
        }
    }

    private fun updateOwnerlink(ownerLink: String) {
        mutableState.update {
            it.copy(ownerLink = ownerLink)
        }
    }

    private fun updateDescription(description: String) {
        mutableState.update {
            it.copy(description = description)
        }
    }

    private fun addProject() = coroutineScope.launch {
        mutableState.update {
            it.copy(isLoading = true)
        }
        try {
            startupsRepository.addProject(
                name = state.value.name,
                tagline = state.value.tagline,
                thumbnail = state.value.thumbnail?.toByteArray(context)?.toBase64(),
                description = state.value.description,
                ownerLink = state.value.ownerLink,
                media = state.value.media.map { it.toByteArray(context)?.toBase64() },
                topics = state.value.topics.filter { it.selected }.map {
                    it.id
                }
            ).catch { collector ->
                mutableState.update {
                    it.copy(error = collector.localizedMessage)
                }
            }.collectLatest { response ->
                when (response) {
                    is ApiResult.Error -> {
                        mutableState.update {
                            it.copy(error = response.exception)
                        }
                    }
                    is ApiResult.Success -> {
                        mutableEffect.emit(Effect.Success(response._data.id))
                    }
                }
            }
        } catch (e: Exception) {
            Napier.e("AddProject", e)
            mutableState.update {
                it.copy(error = e.localizedMessage)
            }
        }

    }

    private fun updateTopics(topic: SelectableTopicUI) {
        val list = mutableState.value.topics.map { selectableTopic ->
            if (selectableTopic.id == topic.id) {
                selectableTopic.selected = !topic.selected
            }
            selectableTopic
        }

        mutableState.update {
            it.copy(topics = list.toList())
        }
    }

    private fun updateThumbnail(thumbnail: Uri?) {
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

    private fun updatedMedia(media: Uri) {
        val medias = mutableState.value.media.toMutableList()
        medias.add(media)

        mutableState.update {
            it.copy(media = medias.toList())
        }
    }

    private fun deleteMedia(position: Int) {
        val medias = mutableState.value.media.toMutableList()
        Napier.e("medias = $medias")
        Napier.e("position = $position")
        medias.removeAt(position)
        Napier.e("deletedMedias = $medias")

        mutableState.update {
            it.copy(media = medias.toList())
        }
    }

    private fun dismissError() {
        mutableState.update {
            it.copy(error = null)
        }
    }
}