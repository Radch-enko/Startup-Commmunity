package com.multi.producthunt.android.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DefaultTopAppBar(
    modifier: Modifier = Modifier,
    title: String?,
    onBack: () -> Unit,
    isPlaceholderVisible: Boolean = false
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
    )
}

@Composable
fun BackButton(onBack: () -> Unit) {
    IconButton(onClick = onBack) {
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
