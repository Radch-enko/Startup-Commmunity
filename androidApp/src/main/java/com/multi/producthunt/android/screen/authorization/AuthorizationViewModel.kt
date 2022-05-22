package com.multi.producthunt.android.screen.authorization

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.multi.producthunt.domain.repository.UserRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.utils.TokenManager
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthorizationViewModel(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager,
) : StateScreenModel<AuthenticationState>(AuthenticationState(AuthenticationMode.SIGN_IN, "", "")) {

    sealed class Event {

        object ToggleMode : Event()

        class UsernameChanged(val username: String) :
            Event()

        class PasswordChanged(val password: String) :
            Event()

        object Authenticate : Event()

        object ErrorDismissed : Event()
    }

    fun sendEvent(authenticationEvent: Event) {
        when (authenticationEvent) {
            Event.ToggleMode -> {
                toggleAuthenticationMode()
            }
            is Event.PasswordChanged -> updatePassword(authenticationEvent.password)
            is Event.UsernameChanged -> updateUsername(authenticationEvent.username)
            Event.Authenticate -> authenticate()
            Event.ErrorDismissed -> dismissError()
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
        mutableState.value = state.value.copy(
            authenticationMode = newAuthenticationMode
        )
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

        mutableState.update {
            it.copy(password = password, passwordRequirements = requirements.toList())
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
                    tokenManager.saveToken(response.data?.token.toString())
                }
            }
        }
    }

    private fun signUpRequest() {
        TODO("Not yet implemented")
    }

    private fun dismissError() {
        mutableState.update {
            it.copy(error = null)
        }
    }
}