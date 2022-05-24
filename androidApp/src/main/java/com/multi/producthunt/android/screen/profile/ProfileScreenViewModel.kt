package com.multi.producthunt.android.screen.profile

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.multi.producthunt.android.screen.authorization.AuthorizationViewModel
import com.multi.producthunt.domain.repository.UserRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.utils.KMMPreference
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileScreenViewModel(
    private val userRepository: UserRepository,
    private val kmmPreference: KMMPreference
) :
    StateScreenModel<ProfileScreenViewModel.State>(State.Loading) {

    sealed class State {
        object Loading : State()
        class Error(val message: String) : State()
        class Data(val userUI: UserUI) : State()
    }


    sealed class Event {
        class PickProfileImage(val byteArray: ByteArray) : Event()
        class PickCoverImage(val byteArray: ByteArray) : Event()
        object ErrorDismissed : Event()
        class OnNameChanged(val newName: String) : Event()
        class OnHeadlineChanged(val newHeadline: String) : Event()
    }

    init {
        loadData()
    }

    fun sendEvent(event: Event) {
        when (event) {
            Event.ErrorDismissed -> dismissError()
            is Event.PickProfileImage -> updateProfileImage(event.byteArray)
            is Event.PickCoverImage -> updateCoverImage(event.byteArray)
            is Event.OnHeadlineChanged -> updateUser(headline = event.newHeadline)
            is Event.OnNameChanged -> updateUser(name = event.newName)
        }
    }

    private fun updateProfileImage(byteArray: ByteArray?) = coroutineScope.launch {
        mutableState.value = State.Loading
        userRepository.uploadProfileImage(
            profileImage = byteArray,
            token = kmmPreference.getString(AuthorizationViewModel.ACCESS_TOKEN)
        ).collectLatest { response ->
            when (response) {
                is ApiResult.Error -> {
                    mutableState.value = State.Error(response.exception)
                }
                is ApiResult.Success -> {
                    mutableState.value = State.Data(
                        response._data.toUI()
                    )
                }
            }
        }
    }

    private fun updateCoverImage(byteArray: ByteArray?) = coroutineScope.launch {
        mutableState.value = State.Loading
        userRepository.uploadCoverImage(
            coverImage = byteArray,
            token = kmmPreference.getString(AuthorizationViewModel.ACCESS_TOKEN)
        ).collectLatest { response ->
            when (response) {
                is ApiResult.Error -> {
                    mutableState.value = State.Error(response.exception)
                }
                is ApiResult.Success -> {
                    mutableState.value = State.Data(
                        response._data.toUI()
                    )
                }
            }
        }
    }

    private fun updateUser(name: String? = null, headline: String? = null) = coroutineScope.launch {
        mutableState.value = State.Loading
        userRepository.updateUser(
            name = name,
            headline = headline,
            token = kmmPreference.getString(AuthorizationViewModel.ACCESS_TOKEN)
        ).collectLatest { response ->
            when (response) {
                is ApiResult.Error -> {
                    mutableState.value = State.Error(response.exception)
                }
                is ApiResult.Success -> {
                    mutableState.value = State.Data(
                        response._data.toUI()
                    )
                }
            }
        }
    }

    private fun loadData() = coroutineScope.launch {
        mutableState.value = State.Loading

        userRepository.me(kmmPreference.getString(AuthorizationViewModel.ACCESS_TOKEN))
            .collectLatest { response ->
                when (response) {
                    is ApiResult.Error -> {
                        mutableState.value = State.Error(response.exception)
                    }
                    is ApiResult.Success -> {
                        mutableState.value = State.Data(
                            response._data.toUI()
                        )
                    }
                }
            }
    }

    private fun dismissError() {
        loadData()
    }
}