package com.multi.producthunt.android.screen.authorization

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.multi.producthunt.domain.repository.UserRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.utils.KMMPreference
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    companion object {
        val ACCESS_TOKEN: String = "ACCESS_TOKEN"
    }

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

        class CoverImageChanged(val url: String) :
            Event()

        class ProfileImageChanged(val url: String) :
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
            is Event.CoverImageChanged -> updateCoverImage(authenticationEvent.url)
            is Event.HeadlineChanged -> updateHeadline(authenticationEvent.headline)
            is Event.NameChanged -> updateName(authenticationEvent.name)
            is Event.ProfileImageChanged -> updateProfileImage(authenticationEvent.url)
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

    private fun updateCoverImage(url: String) {
        mutableState.update {
            it.copy(coverImage = url)
        }
    }

    private fun updateProfileImage(url: String) {
        mutableState.update {
            it.copy(profileImage = url)
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
                    Napier.e("Token: ${response.data?.token}")
                    pref.put(ACCESS_TOKEN, response.data?.token.toString())
                    mutableEffect.emit(Effect.AuthorizationSuccess)
                }
            }
        }
    }

    private fun signUpRequest() = coroutineScope.launch {
        with(state.value) {
            userRepository.register(
                name,
                username,
                headline,
                profileImage,
                coverImage,
                password,
                passwordAgain
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
                        Napier.e("Token: ${response.data}")
                        signInRequest()
                    }
                }
            }
        }

    }

    private fun dismissError() {
        mutableState.update {
            it.copy(error = null)
        }
    }
}