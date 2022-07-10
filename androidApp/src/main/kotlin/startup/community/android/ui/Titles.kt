package startup.community.android.ui

import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TitleMedium(text: String) {
    Text(
        text = text, style = typography.headlineSmall
    )
}