package com.multi.producthunt.android.screen.home

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.multi.producthunt.android.ui.PagingList

class HomeScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel<HomeScreenViewModel>()
        val state by viewModel.state.collectAsState()

        when (state) {
            is HomeScreenViewModel.State.Data -> {
                PagingList((state as HomeScreenViewModel.State.Data).pagingList)
            }
            HomeScreenViewModel.State.Loading -> {
                Text("Loading...")
            }
        }
    }

    @Preview
    @Composable
    private fun PreviewContent() {
        Content()
    }
}