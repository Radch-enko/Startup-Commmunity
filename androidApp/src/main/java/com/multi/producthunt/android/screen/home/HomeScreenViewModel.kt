package com.multi.producthunt.android.screen.home

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.kuuurt.paging.multiplatform.PagingData
import com.multi.producthunt.domain.usecase.GetStartupsUseCase
import com.multi.producthunt.domain.usecase.StartupsRequestType
import com.multi.producthunt.ui.models.StartupUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val useCase: GetStartupsUseCase) :
    StateScreenModel<HomeScreenViewModel.State>(State.Loading) {

    sealed class State {
        object Loading : State()
        class Data(val pagingList: Flow<PagingData<StartupUI>>) : State()
    }

    init {
        loadData()
    }

    private fun loadData() = coroutineScope.launch {
        mutableState.value = State.Loading

        mutableState.value = State.Data(
            pagingList = useCase.getStartupsPagingData(StartupsRequestType.TOP)
        )
    }
}