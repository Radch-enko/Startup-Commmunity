package startup.community.android.screen.settings

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.kuuurt.paging.multiplatform.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import startup.community.shared.domain.usecase.GetStartupsUseCase
import startup.community.shared.ui.models.ProjectUI

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
    }
}