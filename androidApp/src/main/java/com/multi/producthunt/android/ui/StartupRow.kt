package com.multi.producthunt.android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChangeHistory
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.multi.producthunt.android.ui.theme.Shapes
import com.multi.producthunt.ui.models.ProjectUI
import com.multi.producthunt.ui.models.TopicUI

@Composable
fun StartupRow(startup: ProjectUI, placeHolderVisible: Boolean = false,
               onUpvoteClicked: () -> Unit) {
    androidx.compose.material3.Surface(
        shape = Shapes.medium,
        shadowElevation = 4.dp,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = CenterVertically
        ) {
            StartupImage(startup.url)
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = startup.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.placeholder(placeHolderVisible)
                )
                Spacer(modifier = Modifier.height(16.dp))
                MediumText(
                    text = startup.tagline,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    modifier = Modifier.placeholder(placeHolderVisible)
                )
                Spacer(modifier = Modifier.height(16.dp))
                TopicsList(startup.topics, placeHolderVisible)
            }

            UpvoteButton(startup.votesCount, startup.isVoted, placeHolderVisible, onUpvoteClicked = onUpvoteClicked)
        }
    }
}

@Composable
fun UpvoteButton(
    votesCount: Int,
    checked: Boolean,
    placeHolderVisible: Boolean = false,
    onUpvoteClicked: () -> Unit
) {
    var voteChecked by remember {
        mutableStateOf(checked)
    }
    var count by remember {
        mutableStateOf(votesCount)
    }
    Column(
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) // This is mandatory
        {
            onUpvoteClicked()
            voteChecked = !voteChecked

            if (voteChecked) {
                count++
            } else {
                count--
            }
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.ChangeHistory, contentDescription = null,
            modifier = Modifier.placeholder(placeHolderVisible),
            tint = if (voteChecked) MaterialTheme.colorScheme.primary else LocalContentColor.current.copy(
                alpha = LocalContentAlpha.current
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = count.toString(),
            modifier = Modifier.placeholder(placeHolderVisible),
            color = if (voteChecked) MaterialTheme.colorScheme.primary else LocalContentColor.current.copy(
                alpha = LocalContentAlpha.current
            )
        )
    }
}

@Composable
fun StartupImage(url: String?) {
    var imagePlaceHolderVisibility by remember {
        mutableStateOf(true)
    }
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = Modifier
            .size(80.dp)
            .placeholder(imagePlaceHolderVisibility),
        onLoading = {
            imagePlaceHolderVisibility = true
        },
        onSuccess = {
            imagePlaceHolderVisibility = false
        },
        imageLoader = getImageLoader(LocalContext.current),
    )
}

@Composable
fun TopicsList(topics: List<TopicUI>, placeHolderVisible: Boolean = false) {
    LazyRow(content = {
        items(topics) { topic ->
            Text(
                text = topic.title,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.placeholder(placeHolderVisible)
            )
        }
    }, horizontalArrangement = Arrangement.spacedBy(16.dp))
}

@Composable
fun CommentCount(i: Int, placeHolderVisible: Boolean = false) {
    Row {
        Icon(
            Icons.Default.ChatBubble,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        ) // Reflect image for horizontal
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = i.toString(),
            modifier = Modifier.placeholder(placeHolderVisible)
        )
    }
}
