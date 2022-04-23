package com.multi.producthunt.android.screen.home

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.kuuurt.paging.multiplatform.PagingData
import com.multi.producthunt.domain.repository.StartupsRepository
import com.multi.producthunt.domain.usecase.GetStartupsUseCase
import com.multi.producthunt.domain.usecase.StartupsRequestType
import com.multi.producthunt.ui.models.StartupUI
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class HomeScreenViewModel(
    private val useCase: GetStartupsUseCase,
    private val repository: StartupsRepository
) :
    StateScreenModel<HomeScreenViewModel.State>(State.Empty) {

    data class State(
        val isRefreshing: Boolean = false,
        val pagingList: Flow<PagingData<StartupUI>> = emptyFlow()
    ) {
        companion object {
            val Empty = State()
        }
    }

    sealed class Event {
        class Search(val query: String) : Event()
        object Refresh : Event()
    }

    private val mutableSearchQuery = MutableStateFlow("")
    val searchQueryState = mutableSearchQuery.asStateFlow()

    init {
        mutableState.update { it.copy(isRefreshing = true) }
        loadData()
    }

    fun sendEvent(event: Event) {
        when (event) {
            is Event.Search -> mutableSearchQuery.value = event.query
            Event.Refresh -> {
                mutableState.update { it.copy(isRefreshing = true) }
                loadData()
            }
        }
    }


    private fun loadData() = coroutineScope.launch {
        mutableSearchQuery.debounce(1L).collectLatest { query ->

            if (query.isNotBlank()) {
                mutableState.update {
                    it.copy(
                        isRefreshing = false,
                        pagingList = useCase.getSearchableStartupsPagingData(query)
                    )
                }
            } else {
                mutableState.update {
                    it.copy(
                        isRefreshing = false,
                        pagingList = useCase.getStartupsPagingData(StartupsRequestType.TOP)
                    )
                }
            }
        }
    }
}