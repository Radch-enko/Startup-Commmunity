package com.multi.producthunt.android.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartupRow(startup: StartupUI) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(startup.url)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                imageLoader = getImageLoader(LocalContext.current)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = startup.name, style = typography.headlineSmall)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = startup.tagline)
            }
        }
    }
}
