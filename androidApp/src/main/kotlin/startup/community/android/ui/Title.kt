package startup.community.android.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Title(text: String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        androidx.compose.material3.Text(
            text = text,
            style = typography.headlineLarge
        )
    }
}