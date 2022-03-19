package com.multi.producthunt.android.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.multi.producthunt.ui.models.StartupUI
import kotlinx.coroutines.flow.Flow

@Composable
fun PagingList(pagingData: Flow<PagingData<StartupUI>>) {
    val lazyStartupsList = pagingData.collectAsLazyPagingItems()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(lazyStartupsList) { startup ->
            startup?.let {
                Text(text = startup.name)
            }
        }
        lazyStartupsList.apply {
            when {
                loadState.refresh is LoadState.Error -> {
                    val e = lazyStartupsList.loadState.refresh as LoadState.Error
                    item { Text(text = e.error.localizedMessage ?: "Something went wrong") }
                }
            }
        }
    }
}