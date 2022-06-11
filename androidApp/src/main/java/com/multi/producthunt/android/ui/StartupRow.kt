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
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.multi.producthunt.android.R
import com.multi.producthunt.android.ui.theme.Shapes
import com.multi.producthunt.android.ui.theme.bronze
import com.multi.producthunt.android.ui.theme.gold
import com.multi.producthunt.android.ui.theme.silver
import com.multi.producthunt.ui.models.ProjectUI
import com.multi.producthunt.ui.models.TopicUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartupRow(
    startup: ProjectUI, placeHolderVisible: Boolean = false, onUpvoteClicked: () -> Unit,
    onProjectClick: (id: Int) -> Unit = {},
    position: Int? = null,
) {
    val iconColor: Color? = when (position) {
        0 -> gold
        1 -> silver
        2 -> bronze
        else -> null
    }
    androidx.compose.material3.Surface(
        shape = Shapes.medium,
        shadowElevation = 4.dp,
        tonalElevation = 4.dp,
        onClick = {
            onProjectClick(startup.id)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = CenterVertically
        ) {
            StartupImage(startup.url, iconColor)
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

            UpvoteButton(
                startup.votesCount,
                startup.isVoted,
                placeHolderVisible,
                onUpvoteClicked = onUpvoteClicked
            )
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
            Icons.Default.ThumbUp, contentDescription = null,
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
fun StartupImage(url: String?, positionIcon: Color? = null) {
    Box() {
        LoadableImage(
            link = url, modifier = Modifier
                .size(80.dp)
        )
        if (positionIcon != null) {
            Icon(
                painter = painterResource(id = R.drawable.winner_icon),
                contentDescription = null,
                tint = positionIcon,
                modifier = Modifier
                    .size(24.dp)
                    .align(BottomEnd)
            )
        }
    }

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
