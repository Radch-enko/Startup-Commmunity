package startup.community.android.screen.user_projects


import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.kuuurt.paging.multiplatform.PagingData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import startup.community.shared.domain.repository.UserRepository
import startup.community.shared.domain.usecase.AuthorizationUseCase
import startup.community.shared.domain.usecase.GetStartupsUseCase
import startup.community.shared.network.model.ApiResult
import startup.community.shared.ui.models.ProjectUI

class UserProjectsListViewModel(
    private val userId: Int,
    private val useCase: GetStartupsUseCase,
    private val authorizationUseCase: AuthorizationUseCase,
    private val userRepository: UserRepository
) :
    StateScreenModel<UserProjectsListViewModel.State>(State.Empty) {

    data class State(
        val isRefreshing: Boolean = false,
        val error: String? = null,
        val pagingList: Flow<PagingData<ProjectUI>> = emptyFlow(),
        val isAuthorized: Boolean = false,
        val title: String? = null,
        val isMyProjects: Boolean = false
    ) {
        companion object {
            val Empty = State()
        }
    }

    sealed class Event {
        object Refresh : Event()
        class Vote(val projectId: Int) : Event()
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

    fun loadData() = coroutineScope.launch {
        mutableState.update { it.copy(isRefreshing = true) }

        val id = if (userId != 0) userId else authorizationUseCase.getCurrentUserId() ?: 0
        mutableState.update {
            it.copy(
                isRefreshing = false,
                pagingList = useCase.getStartupsPagingData(
                    date = null,
                    makerId = id
                ),
                isAuthorized = authorizationUseCase.isAuthorized(),
                title = userRepository.getUserById(id = id).single().data?.username,
                isMyProjects = userId == 0
            )
        }
    }
}