package com.multi.producthunt.android.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.multi.producthunt.MR
import com.multi.producthunt.ui.models.StartupUI

@Composable
fun StartupsList(
    scrollState: LazyListState = rememberLazyListState(),
    pagingList: LazyPagingItems<StartupUI>,
    firstItemOffset: Float = 0f
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(LocalDensity.current.run { firstItemOffset.toDp() }))
        }
        items(pagingList) { startup ->
            startup?.let {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    StartupRow(startup)
                }
            }
        }
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

            }
            list.loadState.append is LoadState.Loading -> {
                item {
                    LoadStateAppendLoading()
                }
            }
            list.loadState.append is LoadState.Error -> {

            }
        }
    }
}

@Composable
fun LoadStateAppendLoading() {
    StartupRow(startup = StartupUI("", "Test", "test", null, 0, emptyList()))
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
