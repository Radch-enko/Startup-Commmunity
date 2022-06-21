package com.multi.producthunt.android.screen.create_discussion

import android.content.Context
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.multi.producthunt.domain.repository.DiscussionsRepository
import com.multi.producthunt.domain.repository.TopicsRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.ui.models.SelectableTopicUI
import com.multi.producthunt.ui.models.toSelectableUI
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddDiscussionViewModel(
    private val discussionRepository: DiscussionsRepository,
    private val topicsRepository: TopicsRepository,
    private val context: Context,
) :
    StateScreenModel<AddDiscussionState>(
        AddDiscussionState()
    ) {

    sealed class Event {

        class TitleChanged(val title: String) :
            Event()

        class DescriptionChanged(val description: String) :
            Event()

        class TopicChanged(val topic: SelectableTopicUI) :
            Event()

        object Save : Event()

        object ErrorDismissed : Event()
    }

    sealed class Effect {
        class Success(val discussionId: Int) : Effect()
    }

    private val mutableEffect = MutableSharedFlow<Effect>()
    val effect = mutableEffect.asSharedFlow()

    init {
        loadTopics()
    }

    fun sendEvent(event: Event) {
        when (event) {
            is Event.DescriptionChanged -> updateDescription(event.description)
            Event.ErrorDismissed -> dismissError()
            Event.Save -> saveDiscussion()
            is Event.TitleChanged -> updateTitle(event.title)
            is Event.TopicChanged -> updateTopics(event.topic)
        }
    }

    private fun loadTopics() = coroutineScope.launch {
        topicsRepository.getDiscussionTopics().collectLatest { response ->
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
                }
            }
        }
    }

    private fun updateDescription(description: String) {
        mutableState.update {
            it.copy(description = description)
        }
    }

    private fun saveDiscussion() = coroutineScope.launch {
        mutableState.update {
            it.copy(isLoading = true)
        }

        discussionRepository.createDiscussion(
            state.value.title,
            state.value.description,
            state.value.selectedTopics.map { it.id }).catch { collector ->
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

    private fun updateTitle(title: String) {
        mutableState.update {
            it.copy(title = title)
        }
    }

    private fun dismissError() {
        mutableState.update {
            it.copy(error = null, isLoading = false)
        }
    }
}