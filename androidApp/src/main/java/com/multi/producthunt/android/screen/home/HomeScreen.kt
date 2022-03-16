package com.multi.producthunt.android.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.kodein.rememberScreenModel

class HomeScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel<HomeScreenViewModel>()
        val lazyStartupsList = viewModel.getPagingData().collectAsLazyPagingItems()

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(lazyStartupsList) { startup ->
                startup?.let {
                    Text(text = startup.name)
                }
            }
        }
    }

    @Preview
    @Composable
    private fun PreviewContent() {
        Content()
    }
}