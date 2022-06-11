package com.multi.producthunt.android.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

val DarkMaterial3Theme = darkColorScheme()
val LightMaterial3Theme = lightColorScheme()

@Composable
fun ProductHuntMaterial3(
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme = when {
        dynamicColor && isDark -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColor && !isDark -> dynamicLightColorScheme(LocalContext.current)
        isDark -> DarkMaterial3Theme
        else -> LightMaterial3Theme
    }
    MaterialTheme(
        typography = AppTypography,
        content = content,
        colorScheme = colorScheme
    )
}