package com.multi.producthunt.android.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.multi.producthunt.MR
import com.multi.producthunt.ui.models.ProjectUI
import io.github.aakira.napier.Napier

@Composable
fun StartupsList(
    pagingList: LazyPagingItems<ProjectUI>,
    scrollState: LazyListState = rememberLazyListState(),
    firstItemPaddingTop: Dp = 0.dp,
    onUpvoteClicked: (projectId: Int) -> Unit,
    onProjectClick: (id: Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        state = scrollState
    ) {
        item {
            Spacer(modifier = Modifier.height(firstItemPaddingTop))
        }
        this.applyStates(
            pagingList,
            onUpvoteClicked = onUpvoteClicked,
            onProjectClick = onProjectClick
        )
    }
}

private val startupsRowModifier =
    Modifier.padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)

private fun LazyListScope.applyStates(
    list: LazyPagingItems<ProjectUI>,
    onProjectClick: (id: Int) -> Unit,
    onUpvoteClicked: (projectId: Int) -> Unit
) {
    this.apply {
        startupsRow(list, onUpvoteClicked = onUpvoteClicked, onProjectClick = onProjectClick)
        when {
            list.loadState.refresh is LoadState.Error -> {
                val e = list.loadState.refresh as LoadState.Error
                item {
                    LoadStateError(e.error.localizedMessage)
                }
            }
            list.loadState.refresh is LoadState.Loading -> {
                val placeHolderList = (1..5).map { ProjectUI.Placeholder }
                placeholderStartupsList(placeHolderList)
            }
            list.loadState.append is LoadState.Loading -> {
                val placeHolderList = (1..2).map { ProjectUI.Placeholder }
                placeholderStartupsList(placeHolderList)
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

private fun LazyListScope.placeholderStartupsList(
    list: List<ProjectUI>
) {
    this.items(list) { startup ->
        Box(modifier = startupsRowModifier) {
            StartupRow(startup, placeHolderVisible = true, onUpvoteClicked = {})
        }
    }
}

private fun LazyListScope.startupsRow(
    list: LazyPagingItems<ProjectUI>,
    onUpvoteClicked: (projectId: Int) -> Unit,
    onProjectClick: (id: Int) -> Unit
) {
    this.itemsIndexed(list) { _, startup ->
        Box(modifier = startupsRowModifier) {
            if (startup != null) {
                StartupRow(
                    startup,
                    onUpvoteClicked = { onUpvoteClicked(startup.id) },
                    onProjectClick = onProjectClick
                )
            }
        }
    }
}

@Composable
fun NoItemsMessage() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            stringResource(id = MR.strings.no_items_message.resourceId), modifier = Modifier.align(
                Alignment.Center
            )
        )
    }
}

@Composable
fun LoadStateError(localizedMessage: String?) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = localizedMessage
                ?: stringResource(id = MR.strings.something_went_wrong.resourceId),
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoadStateAppendError(localizedMessage: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = localizedMessage
                ?: stringResource(id = MR.strings.something_went_wrong.resourceId),
            textAlign = TextAlign.Center
        )
    }
}