package com.multi.producthunt.android.screen.home

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.kuuurt.paging.multiplatform.PagingData
import com.multi.producthunt.domain.usecase.GetStartupsUseCase
import com.multi.producthunt.ui.models.ProjectUI
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class HomeScreenViewModel(
    private val useCase: GetStartupsUseCase
) :
    StateScreenModel<HomeScreenViewModel.State>(State.Empty) {

    data class State(
        val isRefreshing: Boolean = false,
        val pagingList: Flow<PagingData<ProjectUI>> = emptyFlow()
    ) {
        companion object {
            val Empty = State()
        }
    }

    sealed class Event {
        class Search(val query: String) : Event()
        object Refresh : Event()
    }

    var lastScrollIndex = 0
    val _scrollUp = MutableStateFlow(false)
    val scrollUp: StateFlow<Boolean>
        get() = _scrollUp.asStateFlow()

    private val mutableSearchQuery = MutableStateFlow("")
    val searchQueryState = mutableSearchQuery.asStateFlow()

    init {
        mutableState.update { it.copy(isRefreshing = true) }
        collectQuery()
        loadData()
    }

    private fun collectQuery() = coroutineScope.launch {
        searchQueryState.collectLatest {
            mutableState.update {
                it.copy(isRefreshing = true)
            }
        }
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

    fun updateScrollPosition(newScrollIndex: Int) {
        if (newScrollIndex == lastScrollIndex) return

        _scrollUp.value = newScrollIndex > lastScrollIndex
        lastScrollIndex = newScrollIndex
    }


    private fun loadData() = coroutineScope.launch {
        mutableSearchQuery.distinctUntilChanged(areEquivalent = { old, new -> old == new })
            .debounce(800)
            .collectLatest { query ->
                mutableState.update {
                    it.copy(
                        isRefreshing = false,
                        pagingList = useCase.getStartupsPagingData(query)
                    )
                }
            }
    }
}