package com.multi.producthunt.android.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ButtonDefault(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.then(Modifier.fillMaxWidth(.5f)),
        enabled = enabled
    ) {
        Text(text = text.uppercase())
    }
}

@Composable
fun OutlinedButtonDefault(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(.5f),
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick,
        modifier,
        enabled = enabled,
        shape = CircleShape
    ) {
        Text(text = text.uppercase())
    }
}