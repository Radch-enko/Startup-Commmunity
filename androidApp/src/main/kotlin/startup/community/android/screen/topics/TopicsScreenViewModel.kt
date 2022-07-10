package startup.community.android.screen.topics

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import startup.community.shared.domain.repository.TopicsRepository
import startup.community.shared.network.model.ApiResult
import startup.community.shared.ui.models.DetailTopicUI
import startup.community.shared.ui.models.toDetailTopicUI

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