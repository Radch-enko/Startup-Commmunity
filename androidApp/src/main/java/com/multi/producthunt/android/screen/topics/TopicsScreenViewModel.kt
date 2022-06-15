package com.multi.producthunt.android.screen.topics

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.multi.producthunt.domain.repository.TopicsRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.ui.models.DetailTopicUI
import com.multi.producthunt.ui.models.toDetailTopicUI
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TopicsScreenViewModel(private val topicsRepository: TopicsRepository) :
    StateScreenModel<TopicsScreenViewModel.State>(State.Empty) {

    data class State(
        val isRefreshing: Boolean = false,
        val error: String? = null,
        val topics: List<DetailTopicUI> = emptyList()
    ) {
        companion object {
            val Empty = State()
        }
    }

    init {
        loadData()
    }

    private fun loadData() = coroutineScope.launch {
        mutableState.update {
            it.copy(isRefreshing = true, error = null)
        }
        delay(400)
        topicsRepository.getTopics().collectLatest { response ->
            when (response) {
                is ApiResult.Error -> {
                    mutableState.update {
                        it.copy(error = response.message)
                    }
                }
                is ApiResult.Success -> {
                    mutableState.update {
                        it.copy(
                            isRefreshing = false,
                            topics = response._data.map { topicDomain -> topicDomain.toDetailTopicUI() })
                    }
                }
            }
        }
    }

    fun sendEvent(event: Event) {
        when (event) {
            Event.Refresh -> {
                loadData()
            }
        }
    }

    sealed class Event {
        object Refresh : Event()
    }
}