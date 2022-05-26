package com.multi.producthunt.android.screen.settings

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.kuuurt.paging.multiplatform.PagingData
import com.multi.producthunt.domain.usecase.GetStartupsUseCase
import com.multi.producthunt.ui.models.ProjectUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SettingsScreenViewModel(private val useCase: GetStartupsUseCase) :
    StateScreenModel<SettingsScreenViewModel.State>(State.Loading) {

    sealed class State {
        object Loading : State()
        class Data(val pagingList: Flow<PagingData<ProjectUI>>) : State()
    }

    init {
        loadData()
    }

    private fun loadData() = coroutineScope.launch {
        mutableState.value = State.Loading

        mutableState.value = State.Data(
            pagingList = useCase.getStartupsPagingData()
        )
    }
}