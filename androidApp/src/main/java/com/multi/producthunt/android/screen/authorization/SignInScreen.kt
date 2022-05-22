package com.multi.producthunt.android.screen.authorization

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.kodein.rememberScreenModel

class SignInScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel<AuthorizationViewModel>()
    }
}