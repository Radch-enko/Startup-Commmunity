package com.multi.producthunt.android.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.multi.producthunt.MR
import com.multi.producthunt.android.R
import com.multi.producthunt.android.screen.authorization.AuthenticationScreen
import com.multi.producthunt.android.screen.profile.ProfileScreen
import com.multi.producthunt.android.screen.user_projects.UserProjectsListScreen
import com.multi.producthunt.android.ui.ButtonDefault
import com.multi.producthunt.android.ui.DefaultTopAppBar
import com.multi.producthunt.android.ui.ErrorDialog
import com.multi.producthunt.android.ui.LoadableImage
import com.multi.producthunt.android.ui.MediumText
import com.multi.producthunt.android.ui.OutlinedButtonDefault
import com.multi.producthunt.android.ui.ProgressBar
import com.multi.producthunt.android.ui.TitleMedium
import com.multi.producthunt.android.ui.theme.white
import com.multi.producthunt.ui.models.DetailProjectUI
import com.multi.producthunt.ui.models.TopicUI
import com.multi.producthunt.ui.models.UiComment
import kotlinx.coroutines.flow.collectLatest

class DetailProjectScreen(private val id: Int) : AndroidScreen() {

    @Composable
    override fun Content() {
        val viewModel: DetailProjectViewModel = rememberScreenModel(arg = id)

        val state by viewModel.state.collectAsState()
        val scroll = rememberLazyListState()
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
                    viewModel.sendEvent(DetailProjectViewModel.Event.Retry)
                })
        }

        LaunchedEffect(key1 = null, block = {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    DetailProjectViewModel.Effect.ScrollToBottom -> {
                        scroll.animateScrollToItem(scroll.layoutInfo.totalItemsCount)
                    }
                }
            }
        })

        val systemUiController = rememberSystemUiController()

        val color = MaterialTheme.colorScheme.secondaryContainer

        LifecycleEffect(
            onStarted = {
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

                            Text(
                                text = detailProjectUI.description,
                                style = typography.bodyMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        ProjectTopicsInfo(detailProjectUI.topics)

                        Spacer(modifier = Modifier.height(32.dp))
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
                                    detailProjectUI.makerId,
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
    fun CommentTextField(
        modifier: Modifier = Modifier,
        text: String,
        label: String,
        onValueChanged: (String) -> Unit,
        onCommentSend: () -> Unit,
    ) {
        TextField(
            modifier = modifier
                .padding(16.dp)
                .shadow(4.dp, CutCornerShape(8.dp)),
            value = text,
            onValueChange = onValueChanged,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent,
                focusedLabelColor = Transparent,
                disabledLabelColor = Transparent,
                unfocusedLabelColor = Transparent,
            ),
            placeholder = {
                Text(text = label)
            },
            trailingIcon = {
                IconButton(onClick = onCommentSend, enabled = text.isNotEmpty()) {
                    Icon(Icons.Filled.Send, contentDescription = null)
                }
            })
    }

    @Composable
    fun ProjectTopicsInfo(topics: List<TopicUI>) {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
        ) {
            topics.forEach { topicUI ->
                DetailProjectTopic(topicUI)
            }
        }
    }

    @Composable
    fun DetailProjectTopic(topicUI: TopicUI) {
        Surface(
            color = MaterialTheme.colorScheme.inversePrimary,
            shape = CircleShape,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp),
                text = topicUI.title,
                color = MaterialTheme.colorScheme.inverseOnSurface
            )
        }
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
            Spacer(modifier = Modifier.width(32.dp))
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

    @Composable
    fun ProjectComments(
        comments: List<UiComment>,
        makerId: Int,
        onCommentatorClick: (id: Int) -> Unit
    ) {
        comments.forEach { uiComment ->
            Comment(
                uiComment.text,
                uiComment.user.avatar,
                uiComment.user.name,
                uiComment.user.headline.orEmpty(),
                makerId == uiComment.user.id,
                uiComment.createdAt,
                onCommentatorClick = {
                    onCommentatorClick(uiComment.user.id)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiComment.childComments.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .fillMaxSize()
                ) {
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(2.dp), Gray
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Column {
                        uiComment.childComments.forEach { childComment ->
                            Comment(
                                childComment.text,
                                childComment.user.avatar,
                                childComment.user.name,
                                childComment.user.headline.orEmpty(),
                                makerId == uiComment.user.id,
                                childComment.createdAt,
                                onCommentatorClick = {}
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    @Composable
    fun Comment(
        text: String,
        avatar: String?,
        userName: String,
        userHeadLine: String,
        isMaker: Boolean,
        createdAt: String?,
        onCommentatorClick: () -> Unit
    ) {
        Column(modifier = Modifier.padding(top = 20.dp)) {
            CommentatorCard(avatar, userName, userHeadLine, isMaker, onCommentatorClick)

            Text(
                text = text,
                style = typography.bodyMedium,
                modifier = Modifier.padding(top = 15.dp)
            )

            CommentButtons(createdAt)
        }
    }

    @Composable
    fun CommentButtons(createdAt: String?) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            if (createdAt != null) {
                MediumText(text = createdAt, style = typography.bodySmall)
            }
        }
    }

    @Composable
    fun CommentatorCard(
        avatar: String?,
        userName: String,
        userHeadLine: String,
        isMaker: Boolean,
        onCommentatorClick: () -> Unit
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
            onCommentatorClick()
        }) {

            val makerModifier = if (isMaker) Modifier.border(
                2.dp,
                MaterialTheme.colorScheme.primary,
                CircleShape
            ) else Modifier.border(2.dp, Color.LightGray, CircleShape)

            LoadableImage(
                link = avatar, modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .then(makerModifier),
                errorDrawable = R.drawable.no_profile_image
            )

            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(text = userName, style = typography.titleMedium)

                MediumText(text = userHeadLine)
            }
        }
    }
}