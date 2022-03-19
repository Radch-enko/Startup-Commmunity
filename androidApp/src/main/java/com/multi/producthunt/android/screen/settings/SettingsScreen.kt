package com.multi.producthunt.android.screen.settings

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

class SettingsScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel<SettingsScreenViewModel>()
        val state by viewModel.state.collectAsState()

//        when (state) {
//            is SettingsScreenViewModel.State.Data -> {
//                PagingList((state as SettingsScreenViewModel.State.Data).pagingList)
//            }
//            SettingsScreenViewModel.State.Loading -> {
//                Text("Loading...")
//            }
//        }
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Settings screen", modifier = Modifier.align(Alignment.Center))
        }
    }

    @Preview
    @Composable
    private fun PreviewContent() {
        Content()
    }
}