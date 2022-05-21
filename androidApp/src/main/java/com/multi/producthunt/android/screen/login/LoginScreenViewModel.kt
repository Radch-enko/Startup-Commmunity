package com.multi.producthunt.android.screen.login

import android.content.Context
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.multi.producthunt.domain.repository.UserRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.utils.TokenManager
import com.multi.producthunt.utils.Validator
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager,
    private val validator: Validator,
    private val context: Context
) :
    StateScreenModel<LoginScreenViewModel.State>(State.emptyState) {

    data class State(
        val username: String,
        val password: String,
        val errorMessage: String?,
        val errorVisibility: Boolean
    ) {
        companion object {
            val emptyState =
                State(
                    username = "string",
                    password = "string",
                    errorMessage = null,
                    errorVisibility = false
                )
        }
    }

    sealed class Event {
        class EditUsername(val username: String) : Event()
        class EditPassword(val password: String) : Event()
        object Login : Event()
    }

    fun sendEvent(event: Event) {
        when (event) {
            is Event.Login -> login()
            is Event.EditPassword -> mutableState.update { it.copy(password = event.password) }
            is Event.EditUsername -> mutableState.update { it.copy(username = event.username) }
        }
    }

    private fun login() = coroutineScope.launch {
        try {
            val validationResult =
                validator.isValidLoginData(state.value.username, state.value.password)
                    .toString(context)
            if (validationResult.isEmpty()) {
                userRepository.login(state.value.username, state.value.password).collectLatest { response ->
                    when (response) {
                        is ApiResult.Error -> {
                            mutableState.update { value ->
                                value.copy(
                                    errorMessage = response.exception,
                                    errorVisibility = true
                                )
                            }
                        }
                        is ApiResult.Success -> {
                            Napier.e("Token: ${response.data?.token}")
                            tokenManager.saveToken(response.data?.token.toString())
                        }
                    }
                }
            } else {
                mutableState.update { value ->
                    value.copy(
                        errorMessage = validationResult,
                        errorVisibility = true
                    )
                }
            }
        } catch (e: Exception) {
            Napier.e("LoginViewModel", e)
        }


    }
}