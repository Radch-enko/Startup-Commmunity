package com.multi.producthunt.android.screen.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.multi.producthunt.domain.repository.ReportingRepository
import com.multi.producthunt.domain.repository.StartupsRepository
import com.multi.producthunt.domain.usecase.AuthorizationUseCase
import com.multi.producthunt.domain.usecase.GetStartupsUseCase
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.ui.models.DetailProjectUI
import com.multi.producthunt.ui.models.toDetailUI
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class DetailProjectViewModel(
    private val id: Int,
    private val startupsRepository: StartupsRepository,
    private val useCase: GetStartupsUseCase,
    private val authorizationUseCase: AuthorizationUseCase,
    private val reportingRepository: ReportingRepository
) :
    StateScreenModel<DetailProjectViewModel.State>(State(DetailProjectUI.Empty)) {

    data class State(
        val detailProjectUI: DetailProjectUI,
        val isLoading: Boolean = true,
        val error: String? = null,
        val comment: String = "",
        val isAuthorized: Boolean = false,
    )

    sealed class Event {
        object Retry : Event()
        object DismissError : Event()
        class OnCommentChange(val comment: String) : Event()
        class OnVisitClick(val context: Context) : Event()
        object OnVoteClick : Event()
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
            is Event.OnVisitClick -> doVisit(event.context)
            Event.Retry -> {
                mutableState.update { it.copy(error = null) }
                loadDate(id)
            }
            Event.OnVoteClick -> doVote()
            Event.SendComment -> sendComment()
            is Event.ReportComment -> reportComment(event.id)
            Event.DismissError -> dismissError()
        }
    }

    private fun dismissError() = coroutineScope.launch {
        mutableState.update {
            it.copy(error = null)
        }
        mutableEffect.emit(Effect.Back)
    }

    private fun reportComment(id: Int) = coroutineScope.launch {
        reportingRepository.reportComment(id).collectLatest { response ->
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
        startupsRepository.commentForProject(
            state.value.detailProjectUI.id,
            state.value.comment
        ).collectLatest { response ->
            when (response) {
                is ApiResult.Error -> mutableState.update { it.copy(error = response.exception) }
                is ApiResult.Success -> {
                    mutableState.update {
                        it.copy(
                            detailProjectUI = response._data.toDetailUI(),
                            isLoading = false,
                            comment = ""
                        )
                    }
                    mutableEffect.emit(Effect.ScrollToBottom)
                }
            }
        }
    }

    private fun doVote() = coroutineScope.launch {
        useCase.voteProject(
            state.value.detailProjectUI.id
        ).collectLatest { response ->
            when (response) {
                is ApiResult.Error -> {
                    mutableState.update {
                        it.copy(error = response.exception)
                    }
                }
            }
        }
    }

    private fun doVisit(context: Context) {
        try {
            val url = state.value.detailProjectUI.ownerLink
            val i = Intent(Intent.ACTION_VIEW)
            if (url?.startsWith("http://") == true || url?.startsWith("https://") == true) {
                i.data = Uri.parse(url)
            } else {
                i.data = Uri.parse("http://$url")
            }
            startActivity(context, i, null)
        } catch (e: Exception) {
            Napier.e("DoVisitByLink", e)
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

        startupsRepository.getProjectById(id).collectLatest { response ->
            when (response) {
                is ApiResult.Error -> mutableState.update { it.copy(error = response.exception) }
                is ApiResult.Success -> {
                    mutableState.update {
                        it.copy(
                            detailProjectUI = response._data.toDetailUI(),
                            isLoading = false,
                            isAuthorized = authorizationUseCase.isAuthorized()
                        )
                    }
                }
            }
        }
    }
}