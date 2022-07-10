package startup.community.android.screen.discussions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.kodein.di.compose.rememberInstance
import startup.community.android.R
import startup.community.android.screen.authorization.AuthenticationScreen
import startup.community.android.screen.create_discussion.AddDiscussionScreen
import startup.community.android.screen.detail_discussion.DetailDiscussionScreen
import startup.community.android.ui.DiscussionsList
import startup.community.android.ui.ScrollableSearchField
import startup.community.shared.MR

class DiscussionListScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
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

        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(text = {
                    Text(text = stringResource(id = MR.strings.start_discussion.resourceId))
                }, onClick = {
                    if (state.isAuthorized) {
                        navigator?.push(AddDiscussionScreen())
                    } else {
                        navigator?.push(AuthenticationScreen(onSuccessAuthenticate = { localNavigator ->
                            localNavigator?.pop()
                        }))
                    }

                }, icon = {
                    Icon(Icons.Filled.Forum, null)
                })
            }) { innerPadding ->
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = state.isRefreshing),
                onRefresh = { viewModel.sendEvent(DiscussionListViewModel.Event.Refresh) },
                indicatorPadding = PaddingValues(top = searchFieldHeight),
                modifier = Modifier.padding(innerPadding)
            ) {
                DiscussionsList(
                    pagingList = state.pagingList.collectAsLazyPagingItems(),
                    firstItemPaddingTop = searchFieldHeight,
                    onDiscussionClick = { id ->
                        navigator?.push(DetailDiscussionScreen(id))
                    })
            }

            ScrollableSearchField(
                searchQuery = searchQuery,
                scrollUpState,
                viewModel.lastScrollIndex
            ) {
                viewModel.sendEvent(DiscussionListViewModel.Event.Search(it))
            }
        }


    }
}
