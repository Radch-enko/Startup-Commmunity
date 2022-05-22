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
    enabled: Boolean,
) {
    Button(onClick = onClick, modifier = modifier.then(Modifier.fillMaxWidth(.5f))) {
        Text(text = text.uppercase())
    }
}

@Composable
fun OutlinedButtonDefault(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(onClick, modifier.then(Modifier.fillMaxWidth(.5f)), shape = CircleShape) {
        Text(text = text.uppercase())
    }
}