package com.multi.producthunt.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val DarkMaterial3Theme = darkColorScheme()
val LightMaterial3Theme = lightColorScheme()

@Composable
fun ProductHuntMaterial3(
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        typography = AppTypography,
        content = content,
        colorScheme = if (isDark) DarkMaterial3Theme else LightMaterial3Theme
    )
}