package com.multi.producthunt.android.screen.timeline

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.multi.producthunt.MR
import com.multi.producthunt.android.screen.authorization.AuthenticationScreen
import com.multi.producthunt.android.screen.detail.DetailProjectScreen
import com.multi.producthunt.android.ui.StartupsList
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlin.reflect.KFunction1
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.toKotlinLocalDate

class TimelineScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel<TimelineScreenViewModel>()

        TimelineScreenInner(
            state = viewModel.state.collectAsState().value,
            handleEvent = viewModel::sendEvent
        )

        LifecycleEffect(
            onStarted = {
                viewModel.loadData()
            }
        )

        val dialogState = rememberMaterialDialogState()

        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                positiveButton(stringResource(id = MR.strings.ok_button.resourceId))
                negativeButton(stringResource(id = MR.strings.cancel_button.resourceId))
            },
            shape = CutCornerShape(16.dp),
            backgroundColor = MaterialTheme.colorScheme.background,

            ) {
            datepicker(
                colors = DatePickerDefaults.colors(
                    headerBackgroundColor = MaterialTheme.colorScheme.primary,
                    dateActiveBackgroundColor = MaterialTheme.colorScheme.secondary
                )
            ) { date ->
                viewModel.sendEvent(TimelineScreenViewModel.Event.DatePicked(date.toKotlinLocalDate()))
            }
        }

        LaunchedEffect(key1 = null, block = {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    TimelineScreenViewModel.Effect.ShowPicker -> dialogState.show()
                }
            }
        })
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TimelineScreenInner(
        state: TimelineScreenViewModel.State,
        handleEvent: KFunction1<TimelineScreenViewModel.Event, Unit>
    ) {
        val navigator = LocalNavigator.current?.parent?.parent
        Scaffold(
            topBar = {
                SmallTopAppBar(title = {
                    Text(text = state.title)
                })
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(text = {
                    Text(text = stringResource(id = MR.strings.select_date.resourceId))
                }, onClick = {
                    handleEvent(TimelineScreenViewModel.Event.TogglePicker)
                }, icon = {
                    Icon(Icons.Filled.DateRange, null)
                })
            }) { innerPadding ->
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = state.isRefreshing),
                onRefresh = { handleEvent(TimelineScreenViewModel.Event.Refresh) },
                modifier = Modifier.padding(innerPadding)
            ) {
                StartupsList(state.pagingList.collectAsLazyPagingItems(), onProjectClick = { id ->
                    navigator?.push(DetailProjectScreen(id))
                }, onUpvoteClicked = {
                    if (state.isAuthorized) {
                        handleEvent(TimelineScreenViewModel.Event.Vote(it))
                    } else {
                        navigator?.push(AuthenticationScreen(onSuccessAuthenticate = { localNavigator ->
                            localNavigator?.pop()
                        }))
                    }
                })
            }
        }
    }

    @Preview
    @Composable
    private fun PreviewContent() {
        Content()
    }
}