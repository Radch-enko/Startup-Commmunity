package com.multi.producthunt.android.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.multi.producthunt.MR
import com.multi.producthunt.android.R
import com.multi.producthunt.ui.models.DiscussionItemUI
import com.multi.producthunt.utils.getMyPluralDesc


@Composable
fun DiscussionsList(
    pagingList: LazyPagingItems<DiscussionItemUI>,
    scrollState: LazyListState = rememberLazyListState(),
    firstItemPaddingTop: Dp = 0.dp,
    onDiscussionClick: (id: Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = scrollState
    ) {
        item {
            Spacer(modifier = Modifier.height(firstItemPaddingTop))
            MediumText(
                text = stringResource(id = MR.strings.discussion_explanation.resourceId),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }

        this.applyStates(
            pagingList,
            onDiscussionClick = onDiscussionClick
        )
    }
}

private val discussionRowModifier =
    Modifier.padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)

private fun LazyListScope.applyStates(
    list: LazyPagingItems<DiscussionItemUI>,
    onDiscussionClick: (id: Int) -> Unit
) {
    this.apply {
        discussionRow(list, onDiscussionClick = onDiscussionClick)
        when {
            list.loadState.refresh is LoadState.Error -> {
                val e = list.loadState.refresh as LoadState.Error
                item {
                    LoadStateError(e.error.localizedMessage)
                }
            }
            list.loadState.refresh is LoadState.Loading -> {
                val placeHolderList = (1..5).map { DiscussionItemUI.Placeholder }
                placeholderDiscussionList(placeHolderList)
            }
            list.loadState.append is LoadState.Loading -> {
                val placeHolderList = (1..2).map { DiscussionItemUI.Placeholder }
                placeholderDiscussionList(placeHolderList)
            }
            list.loadState.append is LoadState.Error -> {
                val e = list.loadState.append as LoadState.Error
                item {
                    LoadStateAppendError(e.error.message)
                }
            }
            list.loadState.source.refresh is LoadState.NotLoading && list.loadState.append.endOfPaginationReached && list.itemCount < 1 -> {
                item {
                    NoItemsMessage()
                }
            }
        }
    }
}

private fun LazyListScope.placeholderDiscussionList(
    list: List<DiscussionItemUI>
) {
    this.items(list) { discussion ->
        Box(modifier = discussionRowModifier) {
            DiscussionRow(discussion, placeHolderVisible = true, onDiscussionClick = {})
        }
    }
}

private fun LazyListScope.discussionRow(
    list: LazyPagingItems<DiscussionItemUI>,
    onDiscussionClick: (id: Int) -> Unit
) {
    this.items(list) { discussion ->
        Box(modifier = Modifier) {
            if (discussion != null) {
                DiscussionRow(
                    discussion,
                    onDiscussionClick = { onDiscussionClick(discussion.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscussionRow(
    discussion: DiscussionItemUI,
    placeHolderVisible: Boolean = false,
    onDiscussionClick: () -> Unit
) {
    androidx.compose.material3.Surface(
        color = Color.Transparent,
        onClick = onDiscussionClick,
        modifier = discussionRowModifier
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LoadableImage(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    link = discussion.maker.avatar,
                    errorDrawable = R.drawable.no_profile_image
                )
                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = discussion.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.placeholder(placeHolderVisible)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = discussion.maker.name,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.placeholder(placeHolderVisible)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = getMyPluralDesc(MR.plurals.replies_count, discussion.replies).toString(
                        context = LocalContext.current
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.placeholder(placeHolderVisible)
                )
                discussion.createdDate.let {
                    if (!it.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.placeholder(placeHolderVisible)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(modifier = Modifier.fillMaxWidth())
        }
    }
}
