package startup.community.android.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DefaultTopAppBar(
    modifier: Modifier = Modifier,
    title: String?,
    onBack: () -> Unit,
    isPlaceholderVisible: Boolean = false,
    barColors: TopAppBarColors = TopAppBarDefaults.smallTopAppBarColors()
) {
    SmallTopAppBar(
        modifier = modifier,
        navigationIcon = {
            BackButton(onBack = onBack)
        },
        title = {
            if (title != null) {
                Text(
                    text = title,
                    style = typography.titleLarge,
                    modifier = Modifier.placeholder(isPlaceholderVisible)
                )
            }
        },
        colors = barColors
    )
}

@Composable
fun BackButton(modifier: Modifier = Modifier, onBack: () -> Unit) {
    IconButton(modifier = modifier, onClick = onBack) {
        Icon(Icons.Filled.ArrowBack, contentDescription = null)
    }
}

@Composable
fun DefaultLargeTopBar(modifier: Modifier = Modifier, title: String?, onBack: () -> Unit) {
    LargeTopAppBar(modifier = modifier, title = {
        if (title != null) {
            Text(text = title, style = typography.titleLarge)
        }
    }, navigationIcon = {
        BackButton(onBack = onBack)
    })
}
