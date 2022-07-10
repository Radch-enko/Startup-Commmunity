package startup.community.android.screen.timeline

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.kuuurt.paging.multiplatform.PagingData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import startup.community.shared.domain.usecase.AuthorizationUseCase
import startup.community.shared.domain.usecase.GetStartupsUseCase
import startup.community.shared.network.model.ApiResult
import startup.community.shared.ui.models.ProjectUI
import startup.community.shared.utils.toFullDate

class TimelineScreenViewModel(
    private val useCase: GetStartupsUseCase,
    private val authorizationUseCase: AuthorizationUseCase
) :
    StateScreenModel<TimelineScreenViewModel.State>(State.Empty) {

    private var currentDate: LocalDate = Clock.System.now().toLocalDateTime(
        TimeZone.currentSystemDefault()
    ).date

    data class State(
        val title: String = "",
        val isRefreshing: Boolean = false,
        val error: String? = null,
        val pagingList: Flow<PagingData<ProjectUI>> = emptyFlow(),
        val isAuthorized: Boolean = false
    ) {
        companion object {
            val Empty = State()
        }
    }

    sealed class Event {
        class DatePicked(val date: LocalDate) : Event()
        object Refresh : Event()
        object TogglePicker : Event()
        class Vote(val projectId: Int) : Event()
    }

    sealed class Effect {
        object ShowPicker : Effect()
    }

    private val mutableEffect = MutableSharedFlow<Effect>()
    val effect = mutableEffect.asSharedFlow()


    fun sendEvent(event: Event) {
        when (event) {
            Event.Refresh -> {
                mutableState.update { it.copy(isRefreshing = true) }
                loadData()
            }
            Event.TogglePicker -> togglePicker()
            is Event.DatePicked -> {
                currentDate = event.date
                loadData(event.date)
            }
            is Event.Vote -> voteProject(event.projectId)
        }
    }

    private fun voteProject(projectId: Int) = coroutineScope.launch {
        useCase.voteProject(
            projectId
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

    private fun togglePicker() = coroutineScope.launch {
        mutableEffect.emit(Effect.ShowPicker)
    }

    fun loadData(
        date: LocalDate = currentDate
    ) = coroutineScope.launch {

        mutableState.update {
            it.copy(
                isRefreshing = true,
                title = currentDate.toFullDate()
            )
        }

        delay(500)

        mutableState.update {
            it.copy(
                isRefreshing = false,
                title = date.toFullDate(),
                pagingList = useCase.getStartupsPagingData(date = date),
                isAuthorized = authorizationUseCase.isAuthorized()
            )
        }
    }
}