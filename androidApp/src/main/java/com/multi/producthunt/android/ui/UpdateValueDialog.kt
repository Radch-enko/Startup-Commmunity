package com.multi.producthunt.android.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.multi.producthunt.MR

@Composable
fun UpdateValueDialog(
    modifier: Modifier = Modifier,
    label: String,
    onDismissError: () -> Unit,
    onOkCallback: (newValue: String) -> Unit,
    isVisible: Boolean
) {
    var value by remember {
        mutableStateOf("")
    }
    if (isVisible) {
        AlertDialog(
            modifier = modifier.padding(32.dp),
            onDismissRequest = onDismissError,
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismissError
                    ) {
                        Text(
                            text = stringResource(
                                id = MR.strings.cancel_button.resourceId
                            ).uppercase()
                        )
                    }
                    TextButton(
                        onClick = { onOkCallback(value) }
                    ) {
                        Text(
                            text = stringResource(
                                id = MR.strings.ok_button.resourceId
                            ).uppercase()
                        )
                    }
                }
            },
            title = {},
            text = {
                TextFieldDefault(
                    modifier = Modifier,
                    value = value,
                    onValueChange = { value = it },
                    label = label
                )
            }
        )
    }
}

@Composable
fun RedactButton(isVisible: Boolean, onClick: () -> Unit) {
    AnimatedVisibility(isVisible) {
        IconButton(onClick = onClick) {
            Icon(Icons.Filled.Edit, contentDescription = null)
        }
    }
}