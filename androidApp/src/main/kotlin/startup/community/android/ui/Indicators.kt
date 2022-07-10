package startup.community.android.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import startup.community.shared.MR

@Composable
fun MakerIndicator() {
    Row {
        Text(
            text = stringResource(id = MR.strings.maker_indicator.resourceId),
            color = Green,
            fontSize = 10.sp
        )
    }
}

@Composable
fun HunterIndicator() {
    Row {
        Text(
            text = stringResource(id = MR.strings.hunter_indicator.resourceId),
            color = MaterialTheme.colors.primary,
            fontSize = 10.sp
        )
    }
}