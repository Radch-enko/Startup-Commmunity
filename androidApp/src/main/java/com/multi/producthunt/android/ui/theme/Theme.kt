package com.multi.producthunt.android.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ProductHuntMaterial3(
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        typography = AppTypography,
        content = content
    )
}

@Composable
fun ProductHuntMaterial2(
    content: @Composable() () -> Unit
) {
    androidx.compose.material.MaterialTheme(
        content = content
    )
}