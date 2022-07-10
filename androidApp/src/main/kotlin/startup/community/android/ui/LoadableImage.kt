package startup.community.android.ui

import androidx.annotation.DrawableRes
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import startup.community.android.R

@Composable
fun LoadableImage(
    modifier: Modifier = Modifier, link: String?,
    contentScale: ContentScale = ContentScale.Crop,
    @DrawableRes errorDrawable: Int = R.drawable.image_svgrepo_com,
) {
    var placeholder by remember {
        mutableStateOf(false)
    }
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(link)
            .error(errorDrawable)
            .crossfade(true)
            .build(),
        contentScale = contentScale,
        contentDescription = null,
        modifier = modifier
            .placeholder(placeholder),
        onLoading = {
            placeholder = true
        },
        onSuccess = {
            placeholder = false
        },
        onError = {
            placeholder = false
        },
        imageLoader = getImageLoader(LocalContext.current)
    )
}