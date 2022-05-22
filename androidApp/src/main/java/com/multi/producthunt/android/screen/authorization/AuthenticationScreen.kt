package com.multi.producthunt.android.screen.authorization

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.multi.producthunt.MR
import com.multi.producthunt.android.ui.*

class AuthenticationScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel<AuthorizationViewModel>()

        AuthenticationContent(
            authenticationState = viewModel.state.collectAsState().value,
            handleEvent = viewModel::sendEvent
        )
    }

    @Composable
    fun ErrorMessage(message: String) {
        Text(
            text = message, color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(8.dp)
        )
    }


    @Composable
    fun AuthenticationContent(
        modifier: Modifier = Modifier,
        authenticationState: AuthenticationState,
        handleEvent: (event: AuthorizationViewModel.Event) -> Unit
    ) {

        AuthenticationForm(
            username = authenticationState.username,
            password = authenticationState.password,
            enableAuthentication = authenticationState.isFormValid(),
            completedPasswordRequirements = authenticationState.passwordRequirements,
            OnUsernameChanged = {
                handleEvent(AuthorizationViewModel.Event.UsernameChanged(it))
            },
            authenticationMode = authenticationState.authenticationMode,
            OnPasswordChanged = {
                handleEvent(AuthorizationViewModel.Event.PasswordChanged(it))
            },
            onAuthenticate = {
                handleEvent(AuthorizationViewModel.Event.Authenticate)
            },
            onToggleMode = {
                handleEvent(
                    AuthorizationViewModel.Event.ToggleMode
                )
            }
        )
        authenticationState.error?.let { error ->
            ErrorDialog(
                error = error,
                dismissError = {
                    handleEvent(
                        AuthorizationViewModel.Event.ErrorDismissed
                    )
                }
            )
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AuthenticationForm(
        modifier: Modifier = Modifier,
        authenticationMode: AuthenticationMode,
        username: String,
        password: String,
        completedPasswordRequirements: List<PasswordRequirements>,
        enableAuthentication: Boolean,
        OnUsernameChanged: (username: String) -> Unit,
        OnPasswordChanged: (password: String) -> Unit,
        onAuthenticate: () -> Unit,
        onToggleMode: () -> Unit
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            AuthenticationTitle(authenticationMode = authenticationMode)

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextFieldDefault(
                        modifier = modifier,
                        value = username,
                        label = stringResource(id = MR.strings.enter_username.resourceId),
                        onValueChange = OnUsernameChanged
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PasswordField(
                        value = password, onValueChange = OnPasswordChanged, label = stringResource(
                            id = MR.strings.enter_password.resourceId
                        )
                    )

                    AnimatedVisibility(
                        visible = authenticationMode == AuthenticationMode.SIGN_UP
                    ) {
                        PasswordRequirements(satisfiedRequirements = completedPasswordRequirements)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    AuthenticationButton(
                        enableAuthentication = enableAuthentication,
                        authenticationMode = authenticationMode,
                        onAuthenticate = onAuthenticate
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    ToggleAuthenticationMode(
                        modifier = Modifier.fillMaxWidth(),
                        authenticationMode = authenticationMode,
                        toggleAuthentication = {
                            onToggleMode()
                        }
                    )
                }
            }
        }
    }


    @Composable
    fun AuthenticationTitle(
        modifier: Modifier = Modifier,
        authenticationMode: AuthenticationMode
    ) {
        Text(
            text = stringResource(
                if (authenticationMode == AuthenticationMode.SIGN_IN) {
                    MR.strings.login.resourceId
                } else {
                    MR.strings.create_account.resourceId
                }
            ), fontSize = 24.sp,
            fontWeight = FontWeight.Black
        )
    }

    @Composable
    fun PasswordRequirements(
        modifier: Modifier = Modifier,
        satisfiedRequirements: List<PasswordRequirements>
    ) {
        Column(
            modifier = modifier
        ) {
            PasswordRequirements.values().forEach { requirement ->
                Requirement(
                    message = requirement.label.toString(
                        LocalContext.current
                    ),
                    satisfied = satisfiedRequirements.contains(
                        requirement
                    )
                )
            }
        }
    }

    @Composable
    fun AuthenticationButton(
        modifier: Modifier = Modifier,
        authenticationMode: AuthenticationMode,
        enableAuthentication: Boolean,
        onAuthenticate: () -> Unit
    ) {
        ButtonDefault(
            modifier = modifier,
            onClick = {
                onAuthenticate()
            },
            enabled = enableAuthentication,
            text = stringResource(
                if (authenticationMode ==
                    AuthenticationMode.SIGN_IN
                ) {
                    MR.strings.login.resourceId
                } else {
                    MR.strings.create_account.resourceId
                }
            )
        )
    }


    @Composable
    fun ToggleAuthenticationMode(
        modifier: Modifier = Modifier,
        authenticationMode: AuthenticationMode,
        toggleAuthentication: () -> Unit
    ) {
        OutlinedButtonDefault(
            onClick = {
                toggleAuthentication()
            },
            text = stringResource(
                if (authenticationMode ==
                    AuthenticationMode.SIGN_IN
                ) {
                    MR.strings.create_account.resourceId

                } else {
                    MR.strings.login.resourceId
                }
            )
        )

    }
}