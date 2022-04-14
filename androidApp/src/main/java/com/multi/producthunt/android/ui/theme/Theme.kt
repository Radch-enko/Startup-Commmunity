package com.multi.producthunt.android.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ProductHuntTheme(
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        typography = AppTypography,
        content = content
    )
}