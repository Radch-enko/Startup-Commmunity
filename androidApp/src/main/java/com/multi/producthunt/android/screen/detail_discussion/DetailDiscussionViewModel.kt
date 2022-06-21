package com.multi.producthunt.android.screen.detail_discussion

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.multi.producthunt.domain.repository.DiscussionsRepository
import com.multi.producthunt.domain.usecase.AuthorizationUseCase
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.ui.models.DetailDiscussionUI
import com.multi.producthunt.ui.models.toUI
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailDiscussionViewModel(
    private val id: Int,
    private val discussionsRepository: DiscussionsRepository,
    private val authorizationUseCase: AuthorizationUseCase
) :
    StateScreenModel<DetailDiscussionViewModel.State>(State(DetailDiscussionUI.Placeholder)) {

    data class State(
        val detailDiscussion: DetailDiscussionUI,
        val isLoading: Boolean = true,
        val error: String? = null,
        val comment: String = "",
        val isAuthorized: Boolean = false,
    )

    sealed class Event {
        object Retry : Event()
        class OnCommentChange(val comment: String) : Event()
        object SendComment : Event()
    }

    sealed class Effect {
        object ScrollToBottom : Effect()
    }

    private val mutableEffect = MutableSharedFlow<Effect>()
    val effect = mutableEffect.asSharedFlow()

    fun sendEvent(event: Event) {
        when (event) {
            is Event.OnCommentChange -> changeComment(event.comment)
            Event.Retry -> {
                mutableState.update { it.copy(error = null) }
                loadDate(id)
            }
            Event.SendComment -> sendComment()
        }
    }

    private fun sendComment() = coroutineScope.launch {
        discussionsRepository.commentForDiscussion(
            state.value.detailDiscussion.id,
            state.value.comment
        ).collectLatest { response ->
            when (response) {
                is ApiResult.Error -> mutableState.update { it.copy(error = response.exception) }
                is ApiResult.Success -> {
                    mutableState.update {
                        it.copy(
                            detailDiscussion = response._data.toUI(),
                            isLoading = false,
                            comment = ""
                        )
                    }
                    mutableEffect.emit(Effect.ScrollToBottom)
                }
            }
        }
    }

    private fun changeComment(comment: String) {
        mutableState.update {
            it.copy(comment = comment)
        }
    }

    private fun loadDate(id: Int) = coroutineScope.launch {
        mutableState.update {
            it.copy(isLoading = true)
        }

        discussionsRepository.getDetailDiscussion(id).collectLatest { response ->
            when (response) {
                is ApiResult.Error -> mutableState.update { it.copy(error = response.exception) }
                is ApiResult.Success -> {
                    mutableState.update {
                        it.copy(
                            detailDiscussion = response._data.toUI(),
                            isLoading = false,
                            isAuthorized = authorizationUseCase.isAuthorized()
                        )
                    }
                }
            }
        }
    }

}