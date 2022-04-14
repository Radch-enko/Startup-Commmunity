package com.multi.producthunt.android.screen.timeline

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.kodein.rememberScreenModel

class TimelineScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel<TimelineScreenViewModel>()
        val state by viewModel.state.collectAsState()

//        when (state) {
//            is TimelineScreenViewModel.State.Data -> {
//                PagingList((state as TimelineScreenViewModel.State.Data).pagingList)
//            }
//            TimelineScreenViewModel.State.Loading -> {
//                Text("Loading...")
//            }
//        }
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Timeline screen", modifier = Modifier.align(Alignment.Center))
        }
    }

    @Preview
    @Composable
    private fun PreviewContent() {
        Content()
    }
}