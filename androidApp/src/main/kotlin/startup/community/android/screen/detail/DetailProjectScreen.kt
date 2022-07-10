package startup.community.android.screen.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import startup.community.android.screen.authorization.AuthenticationScreen
import startup.community.android.screen.profile.ProfileScreen
import startup.community.android.screen.user_projects.UserProjectsListScreen
import startup.community.android.ui.theme.white
import startup.community.shared.ui.models.DetailProjectUI
import kotlinx.coroutines.flow.collectLatest
import startup.community.android.ui.*
import startup.community.shared.MR

class DetailProjectScreen(private val id: Int) : AndroidScreen() {

    @Composable
    override fun Content() {
        val viewModel: DetailProjectViewModel = rememberScreenModel(arg = id)

        val state by viewModel.state.collectAsState()
        val scroll = rememberLazyListState()
        val context = LocalContext.current
        val successReport = stringResource(id = MR.strings.successReport.resourceId)
        val navigator = LocalNavigator.current

        when {
            state.isLoading -> {
                ProgressBar()
            }
            else -> {
                DetailProjectInfo(
                    state.detailProjectUI,
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
                    viewModel.sendEvent(DetailProjectViewModel.Event.DismissError)
                })
        }

        LaunchedEffect(key1 = null, block = {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    DetailProjectViewModel.Effect.ScrollToBottom -> {
                        scroll.animateScrollToItem(scroll.layoutInfo.totalItemsCount)
                    }
                    DetailProjectViewModel.Effect.Reported -> {
                        Toast.makeText(context, successReport, Toast.LENGTH_SHORT).show()
                    }
                    DetailProjectViewModel.Effect.Back -> {
                        navigator?.pop()
                    }
                }
            }
        })

        val systemUiController = rememberSystemUiController()

        val color = MaterialTheme.colorScheme.secondaryContainer

        LifecycleEffect(
            onStarted = {
                viewModel.sendEvent(DetailProjectViewModel.Event.Retry)
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
    fun DetailProjectInfo(
        detailProjectUI: DetailProjectUI,
        handleEvent: (DetailProjectViewModel.Event) -> Unit,
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
                    detailProjectUI.media.let {
                        if (it.isNotEmpty()) {
                            DetailProjectMedia(it)
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            ProjectInfoThumbnail(
                                detailProjectUI.name,
                                detailProjectUI.tagline,
                                detailProjectUI.thumbnail
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            val context = LocalContext.current
                            ProjectButtons(
                                detailProjectUI.votesCount,
                                voted = detailProjectUI.isVoted,
                                visitEnable = !detailProjectUI.ownerLink.isNullOrEmpty(),
                                onVisitClick = {
                                    handleEvent(
                                        DetailProjectViewModel.Event.OnVisitClick(
                                            context = context
                                        )
                                    )
                                },
                                onVoteClick = {
                                    if (authorized) {
                                        handleEvent(DetailProjectViewModel.Event.OnVoteClick)
                                    } else {
                                        navigator?.push(
                                            AuthenticationScreen(
                                                onSuccessAuthenticate = { localNavigator ->
                                                    localNavigator?.pop()
                                                })
                                        )
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            detailProjectUI.createdDate?.let {
                                MediumText(
                                    text = stringResource(
                                        id = MR.strings.date_mask.resourceId, it
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = detailProjectUI.description,
                                style = typography.bodyMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        ProjectTopicsInfo(detailProjectUI.topics)

                        Spacer(modifier = Modifier.height(32.dp))

                        Divider(modifier = Modifier.fillMaxWidth())

                        ProjectMaker(detailProjectUI.maker, onMakerClick = {
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

                            if (detailProjectUI.comments.isEmpty()) {
                                MediumText(text = stringResource(id = MR.strings.no_comments.resourceId))
                            } else {
                                ProjectComments(
                                    detailProjectUI.comments,
                                    detailProjectUI.maker.id,
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
                                            DetailProjectViewModel.Event.ReportComment(
                                                commentId
                                            )
                                        )
                                    }
                                )
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
                        DetailProjectViewModel.Event.OnCommentChange(it)
                    )
                }, label = stringResource(id = MR.strings.enter_comment.resourceId),
                onCommentSend = {
                    if (authorized) {
                        handleEvent(DetailProjectViewModel.Event.SendComment)
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
    fun ProjectButtons(
        votesCount: Int,
        voted: Boolean,
        visitEnable: Boolean,
        onVisitClick: () -> Unit,
        onVoteClick: () -> Unit
    ) {
        var votes by remember {
            mutableStateOf(votesCount)
        }

        var checked by remember {
            mutableStateOf(voted)
        }

        val isVotedText = if (checked) MR.strings.delete_vote else MR.strings.vote

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedButtonDefault(
                text = stringResource(id = MR.strings.visit.resourceId),
                onClick = onVisitClick, icon = Icons.Filled.Link, modifier = Modifier.weight(1f),
                enabled = visitEnable
            )
            Spacer(modifier = Modifier.width(8.dp))
            ButtonDefault(
                text = stringResource(id = isVotedText.resourceId) + " $votes",
                onClick = {
                    onVoteClick()
                    checked = !checked
                    if (checked) {
                        votes++
                    } else {
                        votes--
                    }

                }, modifier = Modifier.weight(1f)
            )
        }

    }

    @Composable
    fun ProjectInfoThumbnail(name: String, tagline: String, thumbnail: String?) {
        Row {
            LoadableImage(
                modifier = Modifier.size(80.dp),
                link = thumbnail,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = tagline, style = typography.bodyMedium)
            }
        }
    }


    @Composable
    fun DetailProjectMedia(media: List<String>) {
        Row(modifier = Modifier
            .height(250.dp)
            .horizontalScroll(rememberScrollState()), content = {
            media.forEach { link ->
                MediaImage(link)
            }
        })
    }

    @Composable
    fun MediaImage(link: String) {
        Box(modifier = Modifier.fillMaxSize()) {
            LoadableImage(modifier = Modifier.fillMaxSize(), link = link)
        }
    }
}