package startup.community.android.screen.home

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.kuuurt.paging.multiplatform.PagingData
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import startup.community.shared.domain.usecase.AuthorizationUseCase
import startup.community.shared.domain.usecase.GetStartupsUseCase
import startup.community.shared.domain.usecase.GetUsersUseCase
import startup.community.shared.network.model.ApiResult
import startup.community.shared.ui.models.ProjectUI
import startup.community.shared.ui.models.SearchUserUI

@OptIn(FlowPreview::class)
class HomeScreenViewModel(
    private val useCase: GetStartupsUseCase,
    private val authorizationUseCase: AuthorizationUseCase,
    private val usersUseCase: GetUsersUseCase
) :
    StateScreenModel<HomeScreenViewModel.State>(State.Empty) {

    data class State(
        val isRefreshing: Boolean = false,
        val error: String? = null,
        val pagingList: Data = Data.ProjectList(),
        val isAuthorized: Boolean = false
    ) {
        companion object {
            val Empty = State()
        }
    }


    sealed class Data {
        class ProjectList(val pagingList: Flow<PagingData<ProjectUI>> = emptyFlow()) : Data()
        class UsersList(val pagingList: Flow<PagingData<SearchUserUI>> = emptyFlow()) : Data()
    }

    sealed class Event {
        class Search(val query: String) : Event()
        object Refresh : Event()
        class Vote(val projectId: Int) : Event()
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
                        pagingList = if (query.isNotEmpty() && query[0] == '@') {
                            Data.UsersList(
                                pagingList = usersUseCase.getUsersPagingData(query.substring(1))
                            )
                        } else {
                            Data.ProjectList(
                                pagingList = useCase.getStartupsPagingData(
                                    date = null,
                                    searchQuery = query
                                )
                            )
                        },
                        isAuthorized = authorizationUseCase.isAuthorized()
                    )
                }
            }
    }
}