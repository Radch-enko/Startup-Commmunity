package com.multi.producthunt.android.screen.profile

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.multi.producthunt.android.ui.toBase64
import com.multi.producthunt.domain.repository.UserRepository
import com.multi.producthunt.domain.usecase.AuthorizationUseCase
import com.multi.producthunt.network.model.ApiResult
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileScreenViewModel(
    private val id: Int,
    private val userRepository: UserRepository,
    private val authorizationUseCase: AuthorizationUseCase
) :
    StateScreenModel<ProfileScreenViewModel.State>(State.Loading) {

    sealed class State {
        object Loading : State()
        class Error(val message: String) : State()
        class Data(val userUI: UserUI, val isEditable: Boolean = false) : State()
    }

    sealed class Event {
        class PickProfileImage(val byteArray: ByteArray) : Event()
        class PickCoverImage(val byteArray: ByteArray) : Event()
        object ErrorDismissed : Event()
        object Logout : Event()
        class OnNameChanged(val newName: String) : Event()
        class OnHeadlineChanged(val newHeadline: String) : Event()
    }

    sealed class Effect {
        object SuccessLogout : Effect()
    }

    init {
        loadData()
    }

    private val mutableEffect = MutableSharedFlow<Effect>()
    val effect = mutableEffect.asSharedFlow()

    fun sendEvent(event: Event) {
        when (event) {
            Event.ErrorDismissed -> dismissError()
            is Event.PickProfileImage -> updateUser(profileImage = event.byteArray.toBase64())
            is Event.PickCoverImage -> updateUser(coverImage = event.byteArray.toBase64())
            is Event.OnHeadlineChanged -> updateUser(headline = event.newHeadline)
            is Event.OnNameChanged -> updateUser(name = event.newName)
            Event.Logout -> logout()
        }
    }

    private fun logout() = coroutineScope.launch {
        authorizationUseCase.logout()
        authorizationUseCase.deleteCurrentUserId()
        mutableEffect.emit(Effect.SuccessLogout)
    }

    private fun updateUser(
        name: String? = null,
        headline: String? = null,
        profileImage: String? = null,
        coverImage: String? = null
    ) = coroutineScope.launch {
        mutableState.value = State.Loading
        userRepository.updateUser(
            name = name,
            headline = headline,
            profileImage = profileImage,
            coverImage = coverImage
        ).collectLatest { response ->
            when (response) {
                is ApiResult.Error -> {
                    mutableState.value = State.Error(response.exception)
                }
                is ApiResult.Success -> {
                    mutableState.value = State.Data(
                        response._data.toUI(),
                        isEditable = authorizationUseCase.getCurrentUserId() == response._data.id
                    )
                }
            }
        }
    }

    private fun loadData() = coroutineScope.launch {
        mutableState.value = State.Loading

        id.let {
            if (id == 0) userRepository.me() else userRepository.getUserById(id)
        }.collectLatest { response ->
            when (response) {
                is ApiResult.Error -> {
                    mutableState.value = State.Error(response.exception)
                }
                is ApiResult.Success -> {
                    mutableState.value = State.Data(
                        response._data.toUI(),
                        isEditable = authorizationUseCase.getCurrentUserId() == response._data.id
                    )
                }
            }
        }
    }

    private fun dismissError() {
        loadData()
    }
}