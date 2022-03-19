package com.multi.producthunt.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = Flame,
    primaryVariant = Coral,
    secondary = Coral,
    secondaryVariant = DarkCyan,
    background = White,
    surface = White,
    error = AlabamaCrimson,
    onPrimary = Black,
    onSecondary = Black,
    onBackground = Black,
    onSurface = Black,
    onError = White
)

private val DarkColorPalette = darkColors(
    primary = Rufous,
    primaryVariant = Coral,
    secondary = Coral,
    secondaryVariant = DarkCyan,
    background = OuterSpace,
    surface = RaisinBlack,
    error = AlabamaCrimson,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
    onError = White
)

@get:Composable
val Colors.systemBar: Color
    get() = if (isLight) LightGray else Black

@get:Composable
val Colors.third: Color
    get() = if (isLight) Gray else Gray

@get:Composable
val Colors.thirdVariant: Color
    get() = if (isLight) LightGray else Gray

@get:Composable
val Colors.onThird: Color
    get() = if (isLight) Black else Black

@Composable
fun ProductHuntTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
