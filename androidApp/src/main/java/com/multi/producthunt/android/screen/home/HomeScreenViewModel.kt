package com.multi.producthunt.android.screen.home

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.kuuurt.paging.multiplatform.PagingData
import com.multi.producthunt.domain.usecase.GetStartupsUseCase
import com.multi.producthunt.domain.usecase.StartupsRequestType
import com.multi.producthunt.ui.models.StartupUI
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class HomeScreenViewModel(private val useCase: GetStartupsUseCase) :
    StateScreenModel<HomeScreenViewModel.State>(State.Loading) {

    sealed class State {
        object Loading : State()
        class Data(val pagingList: Flow<PagingData<StartupUI>>) : State()
    }

    sealed class Event {
        class Search(val query: String) : Event()
    }

    private val mutableSearchQuery = MutableStateFlow("")
    val searchQueryState = mutableSearchQuery.asStateFlow()

    init {
        loadData()
    }

    fun sendEvent(event: Event) {
        when (event) {
            is Event.Search -> mutableSearchQuery.value = event.query
        }
    }


    private fun loadData() = coroutineScope.launch {
        mutableState.value = State.Loading

        mutableSearchQuery.debounce(300).distinctUntilChanged().collectLatest { query ->
            mutableState.value = State.Data(
                pagingList = useCase.getStartupsPagingData(StartupsRequestType.TOP)
            )
        }
    }
}