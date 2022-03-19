package com.multi.producthunt.android.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.multi.producthunt.ui.models.StartupUI
import kotlinx.coroutines.flow.Flow


@Composable
fun PagingList(pagingData: Flow<PagingData<StartupUI>>) {
    val lazyStartupsList = pagingData.collectAsLazyPagingItems()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(lazyStartupsList) { startup ->
            startup?.let {
                StartupRow(startup)
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

@Composable
fun StartupRow(startup: StartupUI) {
    Card() {
        Row() {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(startup.url)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Text(text = startup.name)
        }
    }
}
