package com.multi.producthunt.android.screen.topic_projects


import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.kuuurt.paging.multiplatform.PagingData
import com.multi.producthunt.domain.usecase.AuthorizationUseCase
import com.multi.producthunt.domain.usecase.GetStartupsUseCase
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.ui.models.ProjectUI
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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