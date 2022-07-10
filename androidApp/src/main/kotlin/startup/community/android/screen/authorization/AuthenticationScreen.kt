package startup.community.android.screen.authorization

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import startup.community.android.ui.ButtonDefault
import startup.community.android.ui.ErrorDialog
import startup.community.android.ui.OutlinedButtonDefault
import startup.community.android.ui.OutlinedPasswordTextField
import startup.community.android.ui.OutlinedTextFieldDefault
import startup.community.android.ui.ProgressBar
import startup.community.android.ui.Requirement
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.collectLatest
import org.kodein.di.compose.rememberInstance
import startup.community.shared.MR

class AuthenticationScreen(private val onSuccessAuthenticate: (navigator: Navigator?) -> Unit) :
    AndroidScreen() {

    @Composable
    override fun Content() {
        val viewModel: AuthorizationViewModel by rememberInstance()
        val navigator = LocalNavigator.current
        LaunchedEffect(null) {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    AuthorizationViewModel.Effect.AuthorizationSuccess -> {
                        onSuccessAuthenticate(navigator)
                    }
                }
            }
        }


        AuthenticationContent(
            authenticationState = viewModel.state.collectAsState().value,
            handleEvent = viewModel::sendEvent
        )
    }

    @Composable
    fun AuthenticationContent(
        modifier: Modifier = Modifier,
        authenticationState: AuthenticationState,
        handleEvent: (event: AuthorizationViewModel.Event) -> Unit
    ) {
        if (authenticationState.isLoading) {
            ProgressBar()
        } else {
            AuthenticationForm(
                name = authenticationState.name,
                username = authenticationState.username,
                password = authenticationState.password,
                passwordAgain = authenticationState.passwordAgain,
                headline = authenticationState.headline,
                enableAuthentication = authenticationState.isFormValid(),
                enableRegistration = authenticationState.isRegistrationFormValid(),
                completedPasswordRequirements = authenticationState.passwordRequirements,
                OnUsernameChanged = {
                    handleEvent(AuthorizationViewModel.Event.UsernameChanged(it))
                },
                authenticationMode = authenticationState.authenticationMode,
                OnPasswordChanged = {
                    handleEvent(AuthorizationViewModel.Event.PasswordChanged(it))
                },
                OnPasswordAgainChanged = {
                    handleEvent(AuthorizationViewModel.Event.PasswordAgainChanged(it))
                },
                OnNameChanged = {
                    handleEvent(AuthorizationViewModel.Event.NameChanged(it))
                },
                OnHeadlineChanged = {
                    handleEvent(AuthorizationViewModel.Event.HeadlineChanged(it.orEmpty()))
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
        }

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

    @Composable
    fun AuthenticationForm(
        modifier: Modifier = Modifier,
        authenticationMode: AuthenticationMode,
        name: String,
        username: String,
        headline: String? = null,
        password: String,
        passwordAgain: String,
        completedPasswordRequirements: List<PasswordRequirements>,
        enableAuthentication: Boolean,
        enableRegistration: Boolean,
        OnUsernameChanged: (username: String) -> Unit,
        OnPasswordChanged: (password: String) -> Unit,
        OnPasswordAgainChanged: (passwordAgain: String) -> Unit,
        OnNameChanged: (name: String) -> Unit,
        OnHeadlineChanged: (headline: String?) -> Unit,
        onAuthenticate: () -> Unit,
        onToggleMode: () -> Unit
    ) {
        Column(
            modifier = modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            AuthenticationTitle(authenticationMode = authenticationMode)

            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextFieldDefault(
                    modifier = modifier,
                    value = username,
                    label = stringResource(id = MR.strings.enter_username.resourceId),
                    onValueChange = OnUsernameChanged
                )

                AnimatedVisibility(
                    visible = authenticationMode == AuthenticationMode.SIGN_UP
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextFieldDefault(
                            modifier = modifier,
                            value = name,
                            label = stringResource(id = MR.strings.enter_name.resourceId),
                            onValueChange = OnNameChanged
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextFieldDefault(
                            modifier = modifier,
                            value = headline.orEmpty(),
                            label = stringResource(id = MR.strings.enter_headline.resourceId),
                            onValueChange = OnHeadlineChanged
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedPasswordTextField(
                    value = password, onValueChange = OnPasswordChanged
                )

                AnimatedVisibility(
                    visible = authenticationMode == AuthenticationMode.SIGN_UP
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedPasswordTextField(
                            value = passwordAgain, onValueChange = OnPasswordAgainChanged,
                            label = stringResource(id = MR.strings.password_again.resourceId)
                        )

                        PasswordRequirements(satisfiedRequirements = completedPasswordRequirements)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                AuthenticationButton(
                    enableAuthentication = if (authenticationMode ==
                        AuthenticationMode.SIGN_IN
                    ) {
                        enableAuthentication
                    } else {
                        enableRegistration
                    },
                    authenticationMode = authenticationMode,
                    onAuthenticate = onAuthenticate
                )

                ToggleAuthenticationMode(
                    modifier = Modifier.fillMaxWidth(),
                    authenticationMode = authenticationMode,
                    toggleAuthentication = {
                        onToggleMode()
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                val mail = stringResource(id = MR.strings.contact_mail.resourceId)
                val context = LocalContext.current
                ClickableText(
                    text = buildAnnotatedString {
                        append(stringResource(id = MR.strings.forgot_your_password.resourceId))
                        append(" ")
                        pushStringAnnotation(
                            tag = "email",
                            annotation = mail
                        )
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append(stringResource(id = MR.strings.contact_support.resourceId))
                        }
                        pop()
                    },
                    onClick = {

                        try {
                            val i = Intent(Intent.ACTION_SENDTO)
                            i.data = Uri.parse("mailto:$mail")
                            ContextCompat.startActivity(context, i, null)
                        } catch (e: Exception) {
                            Napier.e("DoVisitByLink", e)
                        }
                    })
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
            modifier = modifier,
            verticalArrangement = Arrangement.Center
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