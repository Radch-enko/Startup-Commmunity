package com.multi.producthunt.android.screen.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.multi.producthunt.domain.repository.StartupsRepository
import com.multi.producthunt.network.model.ApiResult
import com.multi.producthunt.ui.models.DetailProjectUI
import com.multi.producthunt.ui.models.toDetailUI
import com.multi.producthunt.utils.KMMPreference
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class DetailProjectViewModel(
    private val id: Int,
    private val startupsRepository: StartupsRepository,
    kmmPreference: KMMPreference
) :
    StateScreenModel<DetailProjectViewModel.State>(State(DetailProjectUI.Empty)) {

    data class State(
        val detailProjectUI: DetailProjectUI,
        val isLoading: Boolean = true,
        val error: String? = null,
        val comment: String = ""
    )

    sealed class Event {
        object Retry : Event()
        class OnCommentChange(val comment: String) : Event()
        class OnVisitClick(val context: Context) : Event()
        object OnVoteClick : Event()
        object SendComment : Event()
    }

    sealed class Effect {
        object ScrollToBottom : Effect()
    }

    private val token = kmmPreference.getString("ACCESS_TOKEN")

    private val mutableEffect = MutableSharedFlow<Effect>()
    val effect = mutableEffect.asSharedFlow()

    init {
        loadDate(id)
    }

    fun sendEvent(event: Event) {
        when (event) {
            is Event.OnCommentChange -> changeComment(event.comment)
            is Event.OnVisitClick -> doVisit(event.context)
            Event.Retry -> loadDate(id)
            Event.OnVoteClick -> doVote()
            Event.SendComment -> sendComment()
        }
    }

    private fun sendComment() = coroutineScope.launch {
        startupsRepository.commentForProject(
            state.value.detailProjectUI.id,
            state.value.comment,
            token
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
        // TODO vote for a project
    }

    private fun doVisit(context: Context) {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(state.value.detailProjectUI.ownerLink)
            startActivity(context, i, null)
        }catch (e: Exception){
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

        startupsRepository.getProjectById(id, token).collectLatest { response ->
            when (response) {
                is ApiResult.Error -> mutableState.update { it.copy(error = response.exception) }
                is ApiResult.Success -> {
                    mutableState.update {
                        it.copy(detailProjectUI = response._data.toDetailUI(), isLoading = false)
                    }
                }
            }
        }
    }
}