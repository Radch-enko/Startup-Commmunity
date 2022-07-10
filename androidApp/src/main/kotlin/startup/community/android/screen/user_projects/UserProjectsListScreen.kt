package startup.community.android.screen.user_projects

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import startup.community.android.screen.addproject.AddProjectScreen
import startup.community.android.screen.authorization.AuthenticationScreen
import startup.community.android.screen.detail.DetailProjectScreen
import startup.community.android.ui.DefaultTopAppBar
import startup.community.android.ui.StartupsList
import startup.community.shared.ui.models.ProjectUI

class UserProjectsListScreen(private val userId: Int) : AndroidScreen() {

    @Composable
    override fun Content() {
        val viewModel: UserProjectsListViewModel = rememberScreenModel(arg = userId)
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.current

        LifecycleEffect(
            onStarted = {
                viewModel.loadData()
            }
        )

        UserProjectsList(
            state.pagingList.collectAsLazyPagingItems(),
            state.title.orEmpty(),
            isRefreshing = state.isRefreshing,
            onProjectClick = { id ->
                if (state.isMyProjects) {
                    navigator?.push(AddProjectScreen(id))
                } else {
                    navigator?.push(DetailProjectScreen(id))
                }
            },
            onUpvoteClick = {
                if (state.isAuthorized) {
                    viewModel.sendEvent(UserProjectsListViewModel.Event.Vote(it))
                } else {
                    navigator?.push(AuthenticationScreen(onSuccessAuthenticate = { localNavigator ->
                        localNavigator?.pop()
                    }))
                }
            },
            onBack = { navigator?.pop() },
            onRefresh = { viewModel.sendEvent(UserProjectsListViewModel.Event.Refresh) }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UserProjectsList(
        lazyStartupsList: LazyPagingItems<ProjectUI>,
        title: String,
        isRefreshing: Boolean,
        onProjectClick: (id: Int) -> Unit,
        onUpvoteClick: (id: Int) -> Unit,
        onBack: () -> Unit,
        onRefresh: () -> Unit,
    ) {
        Scaffold(modifier = Modifier.systemBarsPadding(), topBar = {
            DefaultTopAppBar(title = title, onBack = onBack, isPlaceholderVisible = isRefreshing)
        }) { innerPadding ->
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                onRefresh = onRefresh,
            ) {
                Box(modifier = Modifier.padding(innerPadding)) {
                    StartupsList(
                        lazyStartupsList,
                        onProjectClick = onProjectClick, onUpvoteClicked = onUpvoteClick
                    )
                }
            }

        }
    }
}