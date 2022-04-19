package com.multi.producthunt.android.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.multi.producthunt.MR
import com.multi.producthunt.ui.models.StartupUI

@Composable
fun StartupsList(
    pagingList: LazyPagingItems<StartupUI>,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        this.applyStates(pagingList)
    }
}

private val startupsRowModifier =
    Modifier.padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)

private fun LazyListScope.applyStates(list: LazyPagingItems<StartupUI>) {
    this.apply {
        StartupsRow(list)
        when {
            list.loadState.refresh is LoadState.Error -> {
                val e = list.loadState.refresh as LoadState.Error
                item {
                    LoadStateError(e.error.localizedMessage)
                }
            }
            list.loadState.refresh is LoadState.Loading -> {
                val placeHolderList = (1..5).map { StartupUI.Placeholder }
                PlaceholderStartupsList(placeHolderList)
            }
            list.loadState.append is LoadState.Loading -> {
                val placeHolderList = (1..2).map { StartupUI.Placeholder }
                PlaceholderStartupsList(placeHolderList)
            }
        }
    }
}

private fun LazyListScope.PlaceholderStartupsList(
    list: List<StartupUI>
) {
    this.items(list) { startup ->
        Box(modifier = startupsRowModifier) {
            StartupRow(startup, placeHolderVisible = true)
        }
    }
}

private fun LazyListScope.StartupsRow(
    list: LazyPagingItems<StartupUI>
) {
    this.itemsIndexed(list, key = { _, item ->
        item.id
    }
    ) { index, startup ->
        val curDate = startup?.featuredAt
        var prevDate = curDate
        if (index != 0) {
            prevDate = list[index - 1]?.featuredAt
        } else {
            if (curDate != null) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Title(text = curDate)
                }
            }
        }

        if (prevDate != curDate) {
            if (curDate != null) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Title(text = curDate)
                }
            }
        }

        Box(modifier = startupsRowModifier) {
            if (startup != null) {
                StartupRow(startup)
            }
        }
    }
}

@Composable
fun LoadStateError(localizedMessage: String?) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = localizedMessage
                ?: stringResource(id = MR.strings.something_went_wrong.resourceId)
        )
    }
}
