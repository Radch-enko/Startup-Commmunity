package com.multi.producthunt.android.screen.topic_projects

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
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.multi.producthunt.android.screen.authorization.AuthenticationScreen
import com.multi.producthunt.android.screen.detail.DetailProjectScreen
import com.multi.producthunt.android.ui.DefaultTopAppBar
import com.multi.producthunt.android.ui.StartupsList
import com.multi.producthunt.android.ui.UsersList
import com.multi.producthunt.ui.models.ProjectUI

class TopicProjectsListScreen(private val topicId: Int, private val title: String) :
    AndroidScreen() {

    @Composable
    override fun Content() {
        val viewModel: TopicProjectsListViewModel =
            rememberScreenModel(arg = listOf(topicId, title))
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.current
        TopicProjectsList(
            state.pagingList.collectAsLazyPagingItems(),
            state.title.orEmpty(),
            isRefreshing = state.isRefreshing,
            onProjectClick = { id ->
                navigator?.push(DetailProjectScreen(id))
            },
            onUpvoteClick = {
                if (state.isAuthorized) {
                    viewModel.sendEvent(TopicProjectsListViewModel.Event.Vote(it))
                } else {
                    navigator?.push(AuthenticationScreen(onSuccessAuthenticate = { localNavigator ->
                        localNavigator?.pop()
                    }))
                }
            },
            onBack = { navigator?.pop() },
            onRefresh = { viewModel.sendEvent(TopicProjectsListViewModel.Event.Refresh) }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopicProjectsList(
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