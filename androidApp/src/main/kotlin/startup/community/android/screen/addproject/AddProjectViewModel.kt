package startup.community.android.screen.addproject

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import startup.community.android.ui.toBase64
import startup.community.android.ui.toByteArray
import startup.community.android.ui.uploadMedia
import startup.community.shared.domain.repository.StartupsRepository
import startup.community.shared.domain.repository.TopicsRepository
import startup.community.shared.network.model.ApiResult
import startup.community.shared.ui.models.SelectableTopicUI
import startup.community.shared.ui.models.toSelectableUI

class AddProjectViewModel(
    private val projectToRedact: Int = 0,
    private val startupsRepository: StartupsRepository,
    private val topicsRepository: TopicsRepository,
    private val context: Context,
) :
    StateScreenModel<AddProjectState>(
        AddProjectState()
    ) {

    sealed class Event {

        class NameChanged(val name: String) :
            Event()

        class TaglineChanged(val tagline: String) :
            Event()

        class ThumbnailChanged(val thumbnail: Uri?) :
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

        object SaveProject : Event()

        object ErrorDismissed : Event()
        object DeleteProject : Event()
    }

    sealed class Effect {
        class Success(val projectId: Int) : Effect()
        object SuccessDelete : Effect()
    }

    private val mutableEffect = MutableSharedFlow<Effect>()
    val effect = mutableEffect.asSharedFlow()

    init {
        loadTopics()
    }

    private fun loadProjectToRedact(projectToRedact: Int) = coroutineScope.launch {
        mutableState.update {
            it.copy(isLoading = true)
        }

        startupsRepository.getProjectById(projectToRedact).collectLatest { response ->
            when (response) {
                is ApiResult.Error -> mutableState.update { it.copy(error = response.exception) }
                is ApiResult.Success -> {
                    with(response._data) {
                        mutableState.update { it ->
                            it.copy(
                                isLoading = false,
                                name = name,
                                tagline = tagline,
                                description = description,
                                ownerLink = ownerLink.orEmpty(),
                                thumbnail = thumbnail?.toUri(),
                                media = media.mapNotNull { media -> media?.url?.toUri() },
                                topics = it.topics,
                                selectedTopics = it.topics.map { selectableTopicUi ->
                                    selectableTopicUi.selected =
                                        topics.map { it.id }.contains(selectableTopicUi.id)
                                    selectableTopicUi
                                },
                                isRedact = true
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadTopics() = coroutineScope.launch {
        mutableState.update {
            it.copy(isLoading = true)
        }
        topicsRepository.getTopics().collectLatest { response ->
            when (response) {
                is ApiResult.Error -> {
                    mutableState.update {
                        it.copy(error = response.exception)
                    }
                }
                is ApiResult.Success -> {
                    mutableState.update {
                        it.copy(
                            isLoading = false,
                            topics = response._data.map { it.toSelectableUI() })
                    }

                    if (projectToRedact != 0) {
                        loadProjectToRedact(projectToRedact)
                    }
                }
            }
        }
    }

    fun sendEvent(event: Event) {
        when (event) {
            Event.SaveProject -> saveProject()
            Event.ErrorDismissed -> dismissError()
            is Event.MediaChanged -> updatedMedia(event.media)
            is Event.NameChanged -> updateName(event.name)
            is Event.TaglineChanged -> updateTagline(event.tagline)
            is Event.ThumbnailChanged -> updateThumbnail(event.thumbnail)
            is Event.TopicChanged -> updateTopics(event.topic)
            is Event.DescriptionChanged -> updateDescription(event.description)
            is Event.OwnerLinkChanged -> updateOwnerlink(event.ownerLink)
            is Event.MediaDeleted -> deleteMedia(event.position)
            Event.DeleteProject -> deleteProject()
        }
    }

    private fun deleteProject() = coroutineScope.launch {
        startupsRepository.deleteProject(projectToRedact).collectLatest { response ->
            when (response) {
                is ApiResult.Error -> {
                    mutableState.update {
                        it.copy(error = response.exception)
                    }
                }
                is ApiResult.Success -> {
                    mutableEffect.emit(Effect.SuccessDelete)
                }
            }
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

    private fun saveProject() = coroutineScope.launch {
        mutableState.update {
            it.copy(isLoading = true)
        }

        try {
            projectToRedact.let {
                if (it == 0) {
                    startupsRepository.addProject(
                        name = state.value.name,
                        tagline = state.value.tagline,
                        thumbnail = state.value.thumbnail?.toByteArray(context)?.toBase64(),
                        description = state.value.description,
                        ownerLink = state.value.ownerLink,
                        media = state.value.media.map { it.toByteArray(context)?.toBase64() },
                        topics = state.value.selectedTopics.map { it.id }
                    )
                } else {
                    startupsRepository.updateProject(
                        projectId = projectToRedact,
                        name = state.value.name,
                        tagline = state.value.tagline,
                        thumbnail = state.value.thumbnail.toString().uploadMedia(context),
                        description = state.value.description,
                        ownerLink = state.value.ownerLink,
                        media = state.value.media.map { it.toString().uploadMedia(context) },
                        topics = state.value.topics.filter { it.selected }.map {
                            it.id
                        }
                    )
                }
            }.catch { collector ->
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
            Napier.e("TAG", e)
            e.printStackTrace()
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
            it.copy(topics = list.toList(), selectedTopics = list.filter { it.selected })
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