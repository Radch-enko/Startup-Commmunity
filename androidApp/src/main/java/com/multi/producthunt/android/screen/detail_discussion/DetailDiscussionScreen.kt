package com.multi.producthunt.android.screen.detail_discussion

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.multi.producthunt.MR
import com.multi.producthunt.android.screen.authorization.AuthenticationScreen
import com.multi.producthunt.android.screen.profile.ProfileScreen
import com.multi.producthunt.android.screen.user_projects.UserProjectsListScreen
import com.multi.producthunt.android.ui.*
import com.multi.producthunt.android.ui.theme.white
import com.multi.producthunt.ui.models.DetailDiscussionUI
import com.multi.producthunt.ui.models.UiUserCard
import kotlinx.coroutines.flow.collectLatest

class DetailDiscussionScreen(private val discussionId: Int) : Screen {

    @Composable
    override fun Content() {
        val viewModel: DetailDiscussionViewModel = rememberScreenModel(arg = discussionId)

        val state by viewModel.state.collectAsState()
        val scroll = rememberLazyListState()
        val context = LocalContext.current
        val navigator = LocalNavigator.current
        val successReport = stringResource(id = MR.strings.successReport.resourceId)

        when {
            state.isLoading -> {
                ProgressBar()
            }
            else -> {
                DetailDiscussionInfo(
                    state.detailDiscussion,
                    handleEvent = viewModel::sendEvent,
                    state.comment,
                    scroll,
                    state.isAuthorized
                )
            }
        }

        state.error?.let {
            ErrorDialog(
                error = it,
                dismissError = {
                    viewModel.sendEvent(DetailDiscussionViewModel.Event.DismissError)
                })
        }

        LaunchedEffect(key1 = null, block = {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    DetailDiscussionViewModel.Effect.ScrollToBottom -> {
                        scroll.animateScrollToItem(scroll.layoutInfo.totalItemsCount)
                    }
                    DetailDiscussionViewModel.Effect.Reported -> {
                        Toast.makeText(context, successReport, Toast.LENGTH_SHORT).show()
                    }
                    DetailDiscussionViewModel.Effect.Back -> {
                        navigator?.pop()
                    }
                }
            }
        })

        val systemUiController = rememberSystemUiController()

        val color = MaterialTheme.colorScheme.secondaryContainer

        LifecycleEffect(
            onStarted = {
                viewModel.sendEvent(DetailDiscussionViewModel.Event.Retry)
                systemUiController.setNavigationBarColor(
                    color = color,
                    darkIcons = true
                )
            },
            onDisposed = {
                systemUiController.setNavigationBarColor(
                    color = white,
                    darkIcons = true
                )
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DetailDiscussionInfo(
        detailDiscussion: DetailDiscussionUI,
        handleEvent: (DetailDiscussionViewModel.Event) -> Unit,
        comment: String,
        scroll: LazyListState,
        authorized: Boolean
    ) {
        val navigator = LocalNavigator.current
        Scaffold(modifier = Modifier, topBar = {
            DefaultTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = null,
                onBack = { navigator?.pop() }
            )
        }, content = { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                state = scroll,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                    ) {

                        Column(modifier = Modifier.padding(16.dp)) {

                            DiscussionMaker(detailDiscussion.maker, onMakerClick = {
                                navigator?.push(
                                    ProfileScreen(
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

                            Spacer(modifier = Modifier.height(16.dp))

                            Divider(modifier = Modifier.fillMaxWidth())

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = detailDiscussion.title, fontSize = 24.sp,
                                fontWeight = FontWeight.Black
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = detailDiscussion.description,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            detailDiscussion.createdDate?.let {
                                MediumText(
                                    text = stringResource(
                                        id = MR.strings.date_mask.resourceId, it
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        ProjectTopicsInfo(detailDiscussion.topics)

                        Spacer(modifier = Modifier.height(32.dp))

                        Divider(modifier = Modifier.fillMaxWidth())
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {

                            TitleMedium(text = stringResource(id = MR.strings.discussion.resourceId))

                            Spacer(modifier = Modifier.height(16.dp))

                            if (detailDiscussion.comments.isEmpty()) {
                                MediumText(text = stringResource(id = MR.strings.no_comments.resourceId))
                            } else {
                                ProjectComments(
                                    detailDiscussion.comments,
                                    detailDiscussion.maker.id,
                                    onCommentatorClick = {
                                        navigator?.push(
                                            ProfileScreen(
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
                                    },
                                    onReport = { commentId ->
                                        handleEvent(
                                            DetailDiscussionViewModel.Event.ReportComment(
                                                commentId
                                            )
                                        )
                                    })
                            }
                        }
                    }
                }
            }
        }, bottomBar = {
            CommentTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding(),
                text = comment,
                onValueChanged = {
                    handleEvent(
                        DetailDiscussionViewModel.Event.OnCommentChange(it)
                    )
                }, label = stringResource(id = MR.strings.enter_comment.resourceId),
                onCommentSend = {
                    if (authorized) {
                        handleEvent(DetailDiscussionViewModel.Event.SendComment)
                    } else {
                        navigator?.push(AuthenticationScreen(onSuccessAuthenticate = { localNavigator ->
                            localNavigator?.pop()
                        }))
                    }
                }
            )
        }, containerColor = MaterialTheme.colorScheme.surface
        )
    }

    @Composable
    fun DiscussionMaker(maker: UiUserCard, onMakerClick: (id: Int) -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            TitleMedium(text = stringResource(id = MR.strings.discussion_maker.resourceId))

            Spacer(modifier = Modifier.height(16.dp))

            CommentatorCard(
                maker.avatar,
                maker.username,
                maker.headline.orEmpty(),
                true,
                onCommentatorClick = { onMakerClick(maker.id) }, false, onReport = {})
        }
    }
}