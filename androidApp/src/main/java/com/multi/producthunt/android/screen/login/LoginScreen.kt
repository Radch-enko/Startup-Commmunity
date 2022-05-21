package com.multi.producthunt.android.screen.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.multi.producthunt.MR
import com.multi.producthunt.android.R
import com.multi.producthunt.android.ui.ButtonDefault
import com.multi.producthunt.android.ui.PasswordField
import com.multi.producthunt.android.ui.TextFieldDefault

class LoginScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel<LoginScreenViewModel>()
        val state by viewModel.state.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.product_hunt_horizontal_logo_red),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(.5f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = MR.strings.login_description.resourceId),
                style = typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldDefault(
                value = state.username,
                onValueChange = { viewModel.sendEvent(LoginScreenViewModel.Event.EditUsername(it)) })
            Spacer(modifier = Modifier.height(16.dp))
            PasswordField(
                value = state.password,
                onValueChange = { viewModel.sendEvent(LoginScreenViewModel.Event.EditPassword(it)) })

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedVisibility(visible = state.errorVisibility) {
                ErrorMessage(
                    state.errorMessage ?: stringResource(id = MR.strings.login_error.resourceId)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            ButtonDefault(
                text = stringResource(id = MR.strings.login.resourceId),
                onClick = { viewModel.sendEvent(LoginScreenViewModel.Event.Login) })
        }
    }

    @Composable
    fun ErrorMessage(message: String) {
        Text(
            text = message, color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(8.dp)
        )
    }

    @Preview
    @Composable
    private fun PreviewContent() {
        Content()
    }
}