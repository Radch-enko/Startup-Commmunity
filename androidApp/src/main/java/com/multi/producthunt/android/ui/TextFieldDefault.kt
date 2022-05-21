package com.multi.producthunt.android.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.multi.producthunt.MR
import com.multi.producthunt.android.ui.theme.mountainMeadow


@Composable
fun TextFieldDefault(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val clearButtonVisible = value.isNotEmpty()
    TextField(
        value = value, onValueChange = onValueChange, modifier = modifier.fillMaxWidth(),
        label = { Text(stringResource(id = MR.strings.enter_username.resourceId)) },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = mountainMeadow
        ),
        trailingIcon = {
            if (clearButtonVisible) {
                IconButton(onClick = {
                    onValueChange("")
                }) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                }
            }
        },
        shape = CircleShape
    )
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val clearButtonVisible = value.isNotEmpty()
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.then(Modifier.fillMaxWidth()),
        label = { Text(stringResource(id = MR.strings.enter_password.resourceId)) },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = mountainMeadow
        ),
        trailingIcon = {
            if (clearButtonVisible) {
                IconButton(onClick = {
                    onValueChange("")
                }) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                }
            }
        },
        shape = CircleShape
    )
}