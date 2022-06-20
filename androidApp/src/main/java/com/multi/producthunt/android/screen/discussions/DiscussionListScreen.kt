package com.multi.producthunt.android.screen.discussions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.dimensionResource
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.multi.producthunt.android.R
import com.multi.producthunt.android.ui.DiscussionsList
import com.multi.producthunt.android.ui.ScrollableSearchField
import org.kodein.di.compose.rememberInstance

class DiscussionListScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: DiscussionListViewModel by rememberInstance()
        val state by viewModel.state.collectAsState()

        val searchQuery by viewModel.searchQueryState.collectAsState()
        val navigator = LocalNavigator.current?.parent?.parent

        val searchFieldHeight = dimensionResource(id = R.dimen.searchFieldHeight)
        val scrollState = rememberLazyListState()

        val scrollUpState = viewModel.scrollUp.collectAsState()

        viewModel.updateScrollPosition(scrollState.firstVisibleItemIndex)

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = state.isRefreshing),
            onRefresh = { viewModel.sendEvent(DiscussionListViewModel.Event.Refresh) },
            indicatorPadding = PaddingValues(top = searchFieldHeight)
        ) {
            DiscussionsList(
                pagingList = state.pagingList.collectAsLazyPagingItems(),
                firstItemPaddingTop = searchFieldHeight,
                onDiscussionClick = { })
        }

        ScrollableSearchField(searchQuery = searchQuery, scrollUpState, viewModel.lastScrollIndex) {
            viewModel.sendEvent(DiscussionListViewModel.Event.Search(it))
        }
    }
}
