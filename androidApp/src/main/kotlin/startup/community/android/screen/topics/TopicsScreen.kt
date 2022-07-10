package startup.community.android.screen.topics

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
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
import startup.community.android.screen.topic_projects.TopicProjectsListScreen
import startup.community.android.ui.ButtonDefault
import startup.community.android.ui.ErrorDialog
import startup.community.android.ui.LoadableImage
import startup.community.android.ui.MediumText
import startup.community.shared.MR
import startup.community.shared.ui.models.DetailTopicUI

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

    @OptIn(ExperimentalFoundationApi::class)
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
                    stickyHeader {
                        Surface {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = stringResource(id = MR.strings.topics_tab_title.resourceId),
                                    style = typography.headlineSmall
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                MediumText(text = stringResource(id = MR.strings.topics_screen_explanaiton.resourceId))
                            }
                        }
                    }
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
                Spacer(modifier = Modifier.height(8.dp))
                ButtonDefault(
                    text = stringResource(id = MR.strings.open.resourceId),
                    onClick = { onDetailTopicClick(id, title) },
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
