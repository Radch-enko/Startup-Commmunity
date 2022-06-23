package com.multi.producthunt.android.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.multi.producthunt.MR
import com.multi.producthunt.ui.models.TopicUI
import com.multi.producthunt.ui.models.UiComment
import com.multi.producthunt.ui.models.UiUserCard

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
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = Color.Transparent,
            disabledLabelColor = Color.Transparent,
            unfocusedLabelColor = Color.Transparent,
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
fun ProjectComments(
    comments: List<UiComment>,
    makerId: Int,
    onCommentatorClick: (id: Int) -> Unit,
    onReport: (id: Int) -> Unit,
) {
    comments.forEach { uiComment ->
        Comment(
            uiComment.text,
            uiComment.user.avatar,
            uiComment.user.name,
            uiComment.user.headline.orEmpty(),
            makerId == uiComment.user.id,
            uiComment.createdAt,
            isReported = uiComment.isReported,
            onCommentatorClick = {
                onCommentatorClick(uiComment.user.id)
            },
            onReport = { onReport(uiComment.id) }
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
                        .width(2.dp), Color.Gray
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
                            isReported = childComment.isReported,
                            onCommentatorClick = {},
                            onReport = { onReport(uiComment.id) }
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
    isReported: Boolean,
    onCommentatorClick: () -> Unit,
    onReport: () -> Unit,
) {
    Column(modifier = Modifier.padding(top = 20.dp)) {
        CommentatorCard(
            avatar = avatar,
            userName = userName,
            userHeadLine = userHeadLine,
            isMaker = isMaker,
            onCommentatorClick = onCommentatorClick,
            isReported = isReported,
            onReport = onReport
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 15.dp)
        )

        if (createdAt != null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MediumText(text = createdAt, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun CommentButtons(isReported: Boolean, onReport: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {

        var dropDownExpanded by remember {
            mutableStateOf(false)
        }
        if (!isReported) {
            IconButton(onClick = { dropDownExpanded = true }) {
                Icon(Icons.Filled.MoreVert, contentDescription = null)
                DropdownMenu(
                    expanded = dropDownExpanded,
                    onDismissRequest = { dropDownExpanded = false }) {
                    DropdownMenuItem(text = {
                        MediumText(
                            text = stringResource(id = MR.strings.reportComment.resourceId),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }, onClick = {
                        dropDownExpanded = false
                        onReport()
                    })
                }
            }
        }
    }
}

@Composable
fun CommentatorCard(
    avatar: String?,
    userName: String,
    userHeadLine: String,
    isMaker: Boolean,
    isMoreButtonVisible: Boolean = true,
    onCommentatorClick: () -> Unit, isReported: Boolean, onReport: () -> Unit
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
            errorDrawable = com.multi.producthunt.android.R.drawable.no_profile_image
        )

        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(text = userName, style = MaterialTheme.typography.titleMedium)

            MediumText(text = userHeadLine)
        }

        if (isMoreButtonVisible) {
            CommentButtons(isReported, onReport = onReport)
        }
    }
}

@Composable
fun ProjectMaker(maker: UiUserCard, onMakerClick: (id: Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
        .clickable {
            onMakerClick(maker.id)
        }
        .fillMaxWidth()
        .padding(16.dp)) {

        TitleMedium(text = stringResource(id = MR.strings.maker_indicator.resourceId))

        Spacer(modifier = Modifier.width(10.dp))

        Text(text = maker.username, style = MaterialTheme.typography.titleMedium)
    }
}