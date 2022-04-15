package com.multi.producthunt.android.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.paging.compose.items
import com.multi.producthunt.MR
import com.multi.producthunt.ui.models.StartupUI
import com.multi.producthunt.ui.models.TopicUI

@Composable
fun StartupsList(
    pagingList: LazyPagingItems<StartupUI>,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        this.applyStates(pagingList)
    }
}

private fun LazyListScope.applyStates(list: LazyPagingItems<StartupUI>) {
    this.apply {
        when {
            list.loadState.refresh is LoadState.Error -> {
                val e = list.loadState.refresh as LoadState.Error
                item {
                    LoadStateError(e.error.localizedMessage)
                }
            }
            list.loadState.refresh is LoadState.Loading -> {
                val placeHolderList = (1..5).map {
                    StartupUI(
                        "", "Placeholder", "Placeholder", null, 0,
                        listOf(
                            TopicUI("Placeholder"),
                            TopicUI("Placeholder"),
                            TopicUI("Placeholder")
                        )
                    )
                }
                this.startupsRow(placeHolderList, placeholderVisible = true)
            }
            list.loadState.refresh is LoadState.NotLoading -> {
                this.startupsRow(list)
            }
        }
    }
}

private fun LazyListScope.startupsRow(list: List<StartupUI>, placeholderVisible: Boolean = false) {
    this.items(list) { startup ->
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            StartupRow(startup, placeHolderVisible = placeholderVisible)
        }
    }
}

private fun LazyListScope.startupsRow(
    list: LazyPagingItems<StartupUI>,
    placeholderVisible: Boolean = false
) {
    this.items(list) { startup ->
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            if (startup != null) {
                StartupRow(startup, placeHolderVisible = placeholderVisible)
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
