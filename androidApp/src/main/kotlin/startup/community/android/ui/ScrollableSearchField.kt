package startup.community.android.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
    lastScrollIndex: Int,
    onValueChange: (String) -> Unit
) {
    val position by animateFloatAsState(if (scrollUpState.value) -200f else 0f)
    val elevation by animateDpAsState(if (position > -150f && lastScrollIndex != 0) 4.dp else 0.dp)

    androidx.compose.material3.Surface(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { translationY = (position) },
        shadowElevation = elevation
    ) {
        Column {

            androidx.compose.material3.Surface(
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