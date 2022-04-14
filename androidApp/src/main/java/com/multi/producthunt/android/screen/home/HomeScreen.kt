package com.multi.producthunt.android.screen.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.multi.producthunt.android.ui.SearchField
import com.multi.producthunt.android.ui.StartupsList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeScreen : AndroidScreen() {

    private var lastScrollIndex = 0

    private val mutableScrollUpState = MutableStateFlow(false)
    private val scrollUpState: StateFlow<Boolean>
        get() = mutableScrollUpState.asStateFlow()

    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel<HomeScreenViewModel>()
        val state by viewModel.state.collectAsState()
        val searchQuery by viewModel.searchQueryState.collectAsState()

        when (state) {
            is HomeScreenViewModel.State.Data -> {
                TopStartupsData(state as HomeScreenViewModel.State.Data, searchQuery, onSearch = {
                    viewModel.sendEvent(HomeScreenViewModel.Event.Search(it))
                })
            }
            HomeScreenViewModel.State.Loading -> {
                Text("Loading...")
            }
        }
    }

    @Composable
    fun TopStartupsData(
        state: HomeScreenViewModel.State.Data,
        searchQuery: String,
        onSearch: (String) -> Unit
    ) {
        val lazyStartupsList = state.pagingList.collectAsLazyPagingItems()
        val scrollState = rememberLazyListState()
        val scrollUpState by scrollUpState.collectAsState()

        updateScrollPosition(scrollState.firstVisibleItemIndex)

        StartupsList(scrollState, lazyStartupsList, 185f)

        val position by animateFloatAsState(if (scrollUpState) -180f else 0f)

        ScrollableSearchField(searchQuery, position, onValueChange = onSearch)
    }

    @Composable
    fun ScrollableSearchField(
        searchQuery: String,
        position: Float,
        onValueChange: (String) -> Unit
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer { translationY = (position) },
            color = Color.White,
//            elevation = 4.dp
        ) {
            Column {

                Surface(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color.Transparent
                ) {
                    SearchField(
                        value = searchQuery,
                        onValueChange = onValueChange
                    )
                }
            }
        }


    }

    @Preview
    @Composable
    private fun PreviewContent() {
        Content()
    }

    private fun updateScrollPosition(newScrollIndex: Int) {
        if (newScrollIndex == lastScrollIndex) return

        mutableScrollUpState.value = newScrollIndex > lastScrollIndex
        lastScrollIndex = newScrollIndex
    }
}