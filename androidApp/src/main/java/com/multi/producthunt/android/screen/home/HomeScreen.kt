package com.multi.producthunt.android.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.multi.producthunt.android.ui.StartupsList

class HomeScreen : AndroidScreen() {

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

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = state.isRefreshing),
            onRefresh = { viewModel.sendEvent(HomeScreenViewModel.Event.Refresh) }
        ) {
            StartupsList(lazyStartupsList)
        }

        // TODO show scrollable search field
    }

    @Preview
    @Composable
    private fun PreviewContent() {
        Content()
    }
}