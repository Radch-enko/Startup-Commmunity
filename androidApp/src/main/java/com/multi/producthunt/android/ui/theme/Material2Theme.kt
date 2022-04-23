package com.multi.producthunt.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

val DarkMaterial2Theme = darkColors()
val LightMaterial2Theme = lightColors()

@Composable
fun ProductHuntMaterial2(
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit,
) {
    MaterialTheme(
        content = content,
        colors = if (isDark) DarkMaterial2Theme else LightMaterial2Theme
    )
}