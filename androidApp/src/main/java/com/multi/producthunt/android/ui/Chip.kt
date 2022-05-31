package com.multi.producthunt.android.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CustomChip(
    selected: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onToggle: () -> Unit
) {
    // define properties to the chip
    // such as color, shape, width

    val containerAnimatedColor = animateColorAsState(
        targetValue = when {
            selected -> MaterialTheme.colorScheme.onSurface
            else -> Color.Transparent
        }
    )
    val contentAnimatedColor = animateColorAsState(
        targetValue = when {
            selected -> MaterialTheme.colorScheme.onPrimary
            else -> Color.LightGray
        }
    )
    Button(
        onClick = onToggle,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerAnimatedColor.value,
            contentColor = contentAnimatedColor.value
        ),
        modifier = modifier,
        shape = CircleShape,
        enabled = enabled,
        border = BorderStroke(
            width = 1.dp,
            color = when {
                selected -> MaterialTheme.colorScheme.primary
                else -> Color.LightGray
            }
        )
    ) {
        // Add text to show the data that we passed
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall
        )

    }
}