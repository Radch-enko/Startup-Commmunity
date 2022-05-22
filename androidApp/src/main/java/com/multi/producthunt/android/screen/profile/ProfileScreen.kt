package com.multi.producthunt.android.screen.profile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.kodein.rememberScreenModel

class ProfileScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel<ProfileScreenViewModel>()

        when (val state = viewModel.state.collectAsState().value) {
            is ProfileScreenViewModel.State.Empty -> {
                Text(text = "state.token = ${state.token}")
            }
        }
    }
}