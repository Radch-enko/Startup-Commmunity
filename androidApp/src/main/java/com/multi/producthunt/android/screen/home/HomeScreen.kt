package com.multi.producthunt.android.screen.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.multi.producthunt.android.R
import com.multi.producthunt.android.ui.ScrollableSearchField
import com.multi.producthunt.android.ui.StartupsList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeScreen : AndroidScreen() {

    private var lastScrollIndex = 0

    private val _scrollUp = MutableStateFlow(false)
    val scrollUp: StateFlow<Boolean>
        get() = _scrollUp.asStateFlow()

    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel<HomeScreenViewModel>()

        HomeScreen(viewModel)
    }

    @Composable
    private fun HomeScreen(viewModel: HomeScreenViewModel) {
        val state by viewModel.state.collectAsState()
        val searchQuery by viewModel.searchQueryState.collectAsState()

        val lazyStartupsList = state.pagingList.collectAsLazyPagingItems()

        val searchFieldHeight = dimensionResource(id = R.dimen.searchFieldHeight)
        val scrollState = rememberLazyListState()

        val scrollUpState = scrollUp.collectAsState()

        updateScrollPosition(scrollState.firstVisibleItemIndex)

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = state.isRefreshing),
            onRefresh = { viewModel.sendEvent(HomeScreenViewModel.Event.Refresh) },
            indicatorPadding = PaddingValues(top = searchFieldHeight)
        ) {
            StartupsList(lazyStartupsList, scrollState, searchFieldHeight)
        }

        ScrollableSearchField(searchQuery = searchQuery, scrollUpState, lastScrollIndex) {
            viewModel.sendEvent(HomeScreenViewModel.Event.Search(it))
        }
    }

    fun updateScrollPosition(newScrollIndex: Int) {
        if (newScrollIndex == lastScrollIndex) return

        _scrollUp.value = newScrollIndex > lastScrollIndex
        lastScrollIndex = newScrollIndex
    }

    @Preview
    @Composable
    private fun PreviewContent() {
        Content()
    }
}