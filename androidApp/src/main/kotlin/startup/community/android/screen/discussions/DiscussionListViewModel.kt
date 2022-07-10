package startup.community.android.screen.discussions

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.kuuurt.paging.multiplatform.PagingData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import startup.community.shared.domain.usecase.AuthorizationUseCase
import startup.community.shared.domain.usecase.GetDiscussionsUseCase
import startup.community.shared.ui.models.DiscussionItemUI

class DiscussionListViewModel(
    private val discussionUseCase: GetDiscussionsUseCase,
    private val authorizationUseCase: AuthorizationUseCase,
) :
    StateScreenModel<DiscussionListViewModel.State>(State.Empty) {

    data class State(
        val isRefreshing: Boolean = false,
        val error: String? = null,
        val pagingList: Flow<PagingData<DiscussionItemUI>> = emptyFlow(),
        val isAuthorized: Boolean = false
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

    var lastScrollIndex = 0
    val _scrollUp = MutableStateFlow(false)
    val scrollUp: StateFlow<Boolean>
        get() = _scrollUp.asStateFlow()

    init {
        mutableState.update {
            it.copy(
                isRefreshing = true,
                isAuthorized = authorizationUseCase.isAuthorized()
            )
        }
        collectQuery()
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

    fun updateScrollPosition(newScrollIndex: Int) {
        if (newScrollIndex == lastScrollIndex) return

        _scrollUp.value = newScrollIndex > lastScrollIndex
        lastScrollIndex = newScrollIndex
    }

    private fun collectQuery() = coroutineScope.launch {
        searchQueryState.collectLatest {
            mutableState.update {
                it.copy(isRefreshing = true)
            }
        }
    }

    private fun loadData() = coroutineScope.launch {
        mutableSearchQuery.distinctUntilChanged(areEquivalent = { old, new -> old == new })
            .debounce(800)
            .collectLatest { query ->
                mutableState.update {
                    it.copy(
                        isRefreshing = false,
                        pagingList = discussionUseCase.getDiscussionPagingData(query),
                    )
                }
            }
    }
}
