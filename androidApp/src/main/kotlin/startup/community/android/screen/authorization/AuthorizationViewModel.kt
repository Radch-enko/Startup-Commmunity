package startup.community.android.screen.authorization

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import startup.community.shared.domain.repository.UserRepository
import startup.community.shared.domain.usecase.AuthorizationUseCase
import startup.community.shared.network.model.ApiResult
import startup.community.shared.utils.KMMPreference

class AuthorizationViewModel(
    private val userRepository: UserRepository,
    private val pref: KMMPreference,
) : StateScreenModel<AuthenticationState>(
    AuthenticationState(
        AuthenticationMode.SIGN_IN,
        name = "",
        username = "",
        password = "",
        passwordAgain = ""
    )
) {

    sealed class Event {

        object ToggleMode : Event()

        class UsernameChanged(val username: String) :
            Event()

        class PasswordChanged(val password: String) :
            Event()

        class NameChanged(val name: String) :
            Event()

        class HeadlineChanged(val headline: String) :
            Event()

        class PasswordAgainChanged(val passwordAgain: String) :
            Event()

        object Authenticate : Event()

        object ErrorDismissed : Event()
    }

    sealed class Effect {
        object AuthorizationSuccess : Effect()
    }

    private val mutableEffect = MutableSharedFlow<Effect>()
    val effect = mutableEffect.asSharedFlow()

    fun sendEvent(authenticationEvent: Event) {
        when (authenticationEvent) {
            Event.ToggleMode -> {
                toggleAuthenticationMode()
            }
            is Event.PasswordChanged -> updatePassword(authenticationEvent.password)
            is Event.UsernameChanged -> updateUsername(authenticationEvent.username)
            is Event.PasswordAgainChanged -> updatePasswordAgain(authenticationEvent.passwordAgain)
            Event.Authenticate -> authenticate()
            Event.ErrorDismissed -> dismissError()
            is Event.HeadlineChanged -> updateHeadline(authenticationEvent.headline)
            is Event.NameChanged -> updateName(authenticationEvent.name)
        }
    }

    private fun toggleAuthenticationMode() {
        val authenticationMode = state.value.authenticationMode
        val newAuthenticationMode = if (
            authenticationMode == AuthenticationMode.SIGN_IN
        ) {
            AuthenticationMode.SIGN_UP
        } else {
            AuthenticationMode.SIGN_IN
        }
        mutableState.update {
            AuthenticationState(
                name = "",
                username = "",
                password = "",
                passwordAgain = "",
                authenticationMode = newAuthenticationMode
            )
        }
    }

    private fun updateUsername(username: String) {
        mutableState.update {
            it.copy(username = username)
        }
    }

    private fun updatePassword(password: String) {

        val requirements = mutableListOf<PasswordRequirements>()
        if (password.length > 7) {
            requirements.add(PasswordRequirements.EIGHT_CHARACTERS)
        }
        if (password.any { it.isUpperCase() }) {
            requirements.add(PasswordRequirements.CAPITAL_LETTER)
        }
        if (password.any { it.isDigit() }) {
            requirements.add(PasswordRequirements.NUMBER)
        }
        if (password == state.value.passwordAgain) {
            requirements.add(PasswordRequirements.PASSWORDS_ARE_SAME)
        }

        mutableState.update {
            it.copy(password = password, passwordRequirements = requirements.toList())
        }
    }

    private fun updatePasswordAgain(passwordAgain: String) {

        val requirements = mutableListOf<PasswordRequirements>()
        if (state.value.password.length > 7) {
            requirements.add(PasswordRequirements.EIGHT_CHARACTERS)
        }
        if (state.value.password.any { it.isUpperCase() }) {
            requirements.add(PasswordRequirements.CAPITAL_LETTER)
        }
        if (state.value.password.any { it.isDigit() }) {
            requirements.add(PasswordRequirements.NUMBER)
        }
        if (passwordAgain == state.value.password) {
            requirements.add(PasswordRequirements.PASSWORDS_ARE_SAME)
        }

        mutableState.update {
            it.copy(passwordAgain = passwordAgain, passwordRequirements = requirements.toList())
        }
    }

    private fun updateName(name: String) {
        mutableState.update {
            it.copy(name = name)
        }
    }

    private fun updateHeadline(headline: String) {
        mutableState.update {
            it.copy(headline = headline)
        }
    }

    private fun authenticate() {
        mutableState.update {
            it.copy(isLoading = true)
        }

        when (state.value.authenticationMode) {
            AuthenticationMode.SIGN_UP -> signUpRequest()
            AuthenticationMode.SIGN_IN -> signInRequest()
        }
    }

    private fun signInRequest() = coroutineScope.launch {
        userRepository.login(state.value.username, state.value.password).collectLatest { response ->
            when (response) {
                is ApiResult.Error -> {
                    mutableState.update { value ->
                        value.copy(
                            error = response.exception,
                        )
                    }
                }
                is ApiResult.Success -> {
                    pref.put(AuthorizationUseCase.ACCESS_TOKEN, response._data.token)
                    saveCurrentUserId()
                }
            }
        }
    }

    private fun saveCurrentUserId() = coroutineScope.launch {
        userRepository.me().collectLatest { response ->
            when (response) {
                is ApiResult.Error -> mutableState.update { value ->
                    value.copy(
                        error = response.exception,
                    )
                }
                is ApiResult.Success -> {
                    pref.put(AuthorizationUseCase.CURRENT_USER_ID, response._data.id)
                    mutableEffect.emit(Effect.AuthorizationSuccess)
                }
            }
        }
    }

    private fun signUpRequest() = coroutineScope.launch {
        with(state.value) {
            userRepository.register(
                name = name,
                username = username,
                headline = headline,
                password = password,
                password2 = passwordAgain
            ).collectLatest { response ->
                when (response) {
                    is ApiResult.Error -> {
                        mutableState.update { value ->
                            value.copy(
                                error = response.exception,
                            )
                        }
                    }
                    is ApiResult.Success -> {
                        Napier.e("User: ${response.data}")
                        signInRequest()
                    }
                }
            }
        }
    }

    private fun dismissError() {
        mutableState.update {
            it.copy(
                name = "",
                username = "",
                password = "",
                isLoading = false,
                passwordAgain = "", error = null
            )
        }
    }
}