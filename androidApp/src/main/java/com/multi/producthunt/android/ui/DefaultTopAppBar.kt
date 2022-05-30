package com.multi.producthunt.android.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DefaultTopAppBar(modifier: Modifier = Modifier, title: String?, onBack: () -> Unit) {
    SmallTopAppBar(modifier = modifier, navigationIcon = {
        BackButton(onBack = onBack)
    }, title = {
        if (title != null) {
            Text(text = title, style = typography.titleLarge)
        }
    }, colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
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
