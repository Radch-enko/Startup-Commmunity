package startup.community.android.screen.detail_discussion

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import startup.community.shared.domain.repository.DiscussionsRepository
import startup.community.shared.domain.repository.ReportingRepository
import startup.community.shared.domain.usecase.AuthorizationUseCase
import startup.community.shared.network.model.ApiResult
import startup.community.shared.ui.models.DetailDiscussionUI
import startup.community.shared.ui.models.toUI

class DetailDiscussionViewModel(
    private val id: Int,
    private val discussionsRepository: DiscussionsRepository,
    private val authorizationUseCase: AuthorizationUseCase,
    private val reportingRepository: ReportingRepository
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
        object DismissError : Event()
        class OnCommentChange(val comment: String) : Event()
        object SendComment : Event()
        class ReportComment(val id: Int) : Event()
    }

    sealed class Effect {
        object ScrollToBottom : Effect()
        object Reported : Effect()
        object Back : Effect()
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
            Event.DismissError -> dismissError()
            is Event.ReportComment -> reportComment(event.id)
        }
    }

    private fun dismissError() = coroutineScope.launch {
        mutableState.update {
            it.copy(error = null)
        }
        mutableEffect.emit(Effect.Back)
    }

    private fun reportComment(id: Int) = coroutineScope.launch {
        reportingRepository.reportComment(id, true).collectLatest { response ->
            when (response) {
                is ApiResult.Error -> mutableState.update { it.copy(error = response.exception) }
                is ApiResult.Success -> {
                    mutableEffect.emit(Effect.Reported)
                    sendEvent(Event.Retry)
                }
            }
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