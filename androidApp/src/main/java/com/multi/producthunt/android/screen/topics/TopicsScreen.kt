package com.multi.producthunt.android.screen.topics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.multi.producthunt.MR
import com.multi.producthunt.android.screen.topic_projects.TopicProjectsListScreen
import com.multi.producthunt.android.ui.ButtonDefault
import com.multi.producthunt.android.ui.ErrorDialog
import com.multi.producthunt.android.ui.LoadableImage
import com.multi.producthunt.ui.models.DetailTopicUI

class TopicsScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel<TopicsScreenViewModel>()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.current?.parent?.parent

        DetailTopicsInner(
            state.topics,
            state.isRefreshing,
            onDetailTopicClick = { id, title ->
                navigator?.push(TopicProjectsListScreen(id, title))
            },
            onRefresh = { viewModel.sendEvent(TopicsScreenViewModel.Event.Refresh) }
        )

        state.error?.let {
            ErrorDialog(
                error = it,
                dismissError = {
                    viewModel.sendEvent(TopicsScreenViewModel.Event.Refresh)
                })
        }
    }

    @Composable
    fun DetailTopicsInner(
        topics: List<DetailTopicUI>,
        refreshing: Boolean,
        onDetailTopicClick: (topicId: Int, title: String) -> Unit,
        onRefresh: () -> Unit
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = refreshing),
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize(),
            indicatorAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                content = {
                    items(topics) { topic ->
                        DetailTopicCard(
                            topic.id,
                            topic.title,
                            topic.description,
                            topic.image,
                            onDetailTopicClick
                        )
                    }
                },
                verticalArrangement = Arrangement.spacedBy(16.dp)
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DetailTopicCard(
        id: Int,
        title: String,
        description: String,
        image: String,
        onDetailTopicClick: (topicId: Int, title: String) -> Unit
    ) {
        OutlinedCard(modifier = Modifier.padding(16.dp)) {
            LoadableImage(
                link = image, modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = title, style = typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = description, style = typography.bodyMedium)

                ButtonDefault(
                    text = stringResource(id = MR.strings.open.resourceId),
                    onClick = { onDetailTopicClick(id, title) },
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
