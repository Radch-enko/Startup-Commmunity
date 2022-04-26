package com.multi.producthunt.android.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun ScrollableSearchField(
    searchQuery: String,
    scrollUpState: State<Boolean>,
    onValueChange: (String) -> Unit
) {
    val position by animateFloatAsState(if (scrollUpState.value) -200f else 0f)

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