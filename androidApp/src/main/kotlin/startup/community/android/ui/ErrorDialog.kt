package startup.community.android.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import startup.community.shared.MR

@Composable
fun ErrorDialog(
    modifier: Modifier = Modifier,
    error: String,
    dismissError: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            dismissError()
        },
        buttons = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                TextButton(
                    onClick = {
                        dismissError()
                    }
                ) {
                    Text(
                        text = stringResource(
                            id = MR.strings.ok_button.resourceId
                        ).uppercase()
                    )
                }
            }
        },
        title = {
            Text(
                text = stringResource(
                    id = MR.strings.error_title.resourceId
                ),
                fontSize = 18.sp
            )
        },
        text = {
            Text(
                text = error
            )
        }
    )
}