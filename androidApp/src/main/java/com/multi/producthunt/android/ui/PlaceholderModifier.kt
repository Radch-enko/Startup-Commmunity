package com.multi.producthunt.android.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
fun Modifier.placeholder(isVisible: Boolean): Modifier {
    return this.placeholder(isVisible, highlight = PlaceholderHighlight.shimmer())
}