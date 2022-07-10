package startup.community.android.screen.topic_projects


import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.kuuurt.paging.multiplatform.PagingData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import startup.community.shared.domain.usecase.AuthorizationUseCase
import startup.community.shared.domain.usecase.GetStartupsUseCase
import startup.community.shared.network.model.ApiResult
import startup.community.shared.ui.models.ProjectUI

class TopicProjectsListViewModel(
    private val topicId: Int,
    private val title: String,
    private val useCase: GetStartupsUseCase,
    private val authorizationUseCase: AuthorizationUseCase
) :
    StateScreenModel<TopicProjectsListViewModel.State>(State.Empty) {

    data class State(
        val isRefreshing: Boolean = false,
        val error: String? = null,
        val pagingList: Flow<PagingData<ProjectUI>> = emptyFlow(),
        val isAuthorized: Boolean = false,
        val title: String? = null
    ) {
        companion object {
            val Empty = State()
        }
    }

    sealed class Event {
        object Refresh : Event()
        class Vote(val projectId: Int) : Event()
    }

    init {
        loadData()
    }

    fun sendEvent(event: Event) {
        when (event) {
            Event.Refresh -> {
                mutableState.update { it.copy(isRefreshing = true) }
                loadData()
            }
            is Event.Vote -> voteProject(event.projectId)
        }
    }

    private fun voteProject(projectId: Int) = coroutineScope.launch {
        useCase.voteProject(
            projectId
        ).collectLatest { response ->
            when (response) {
                is ApiResult.Error -> {
                    mutableState.update {
                        it.copy(error = response.exception)
                    }
                }
            }
        }
    }

    private fun loadData() = coroutineScope.launch {
        mutableState.update { it.copy(isRefreshing = true) }

        delay(400)
        mutableState.update {
            it.copy(
                isRefreshing = false,
                pagingList = useCase.getStartupsPagingData(
                    date = null,
                    topicId = topicId
                ),
                isAuthorized = authorizationUseCase.isAuthorized(),
                title = title,
            )
        }
    }
}