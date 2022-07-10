package startup.community.android.screen.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.kodein.di.compose.rememberInstance
import startup.community.android.R
import startup.community.android.screen.authorization.AuthenticationScreen
import startup.community.android.screen.detail.DetailProjectScreen
import startup.community.android.screen.profile.ProfileScreen
import startup.community.android.screen.user_projects.UserProjectsListScreen
import startup.community.android.ui.ScrollableSearchField
import startup.community.android.ui.StartupsList
import startup.community.android.ui.UsersList

class HomeScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val viewModel: HomeScreenViewModel by rememberInstance()

        HomeScreenInner(viewModel)
    }

    @Composable
    private fun HomeScreenInner(viewModel: HomeScreenViewModel) {
        val state by viewModel.state.collectAsState()
        val searchQuery by viewModel.searchQueryState.collectAsState()
        val navigator = LocalNavigator.current?.parent?.parent

        val searchFieldHeight = dimensionResource(id = R.dimen.searchFieldHeight)
        val scrollState = rememberLazyListState()

        val scrollUpState = viewModel.scrollUp.collectAsState()

        viewModel.updateScrollPosition(scrollState.firstVisibleItemIndex)

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = state.isRefreshing),
            onRefresh = { viewModel.sendEvent(HomeScreenViewModel.Event.Refresh) },
            indicatorPadding = PaddingValues(top = searchFieldHeight)
        ) {
            when (val list = state.pagingList) {
                is HomeScreenViewModel.Data.ProjectList -> {
                    StartupsList(
                        list.pagingList.collectAsLazyPagingItems(),
                        firstItemPaddingTop = searchFieldHeight,
                        onProjectClick = { id ->
                            navigator?.push(DetailProjectScreen(id))
                        }, onUpvoteClicked = {
                            if (state.isAuthorized) {
                                viewModel.sendEvent(HomeScreenViewModel.Event.Vote(it))
                            } else {
                                navigator?.push(AuthenticationScreen(onSuccessAuthenticate = { localNavigator ->
                                    localNavigator?.pop()
                                }))
                            }
                        })
                }
                is HomeScreenViewModel.Data.UsersList -> {
                    UsersList(
                        pagingList = list.pagingList.collectAsLazyPagingItems(),
                        firstItemPaddingTop = searchFieldHeight,
                        onUserClick = {
                            navigator?.push(ProfileScreen(
                                it,
                                onLogout = { localNavigator ->
                                    localNavigator?.pop()
                                }, onShowProjects = { id, localNavigator ->
                                    localNavigator?.push(
                                        UserProjectsListScreen(
                                            id
                                        )
                                    )
                                })
                            )
                        })
                }
            }

        }

        ScrollableSearchField(searchQuery = searchQuery, scrollUpState, viewModel.lastScrollIndex) {
            viewModel.sendEvent(HomeScreenViewModel.Event.Search(it))
        }
    }

    @Preview
    @Composable
    private fun PreviewContent() {
        Content()
    }
}