package com.multi.producthunt.android.ui

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.multi.producthunt.android.R

@Composable
fun LoadableImage(
    modifier: Modifier = Modifier, link: String?,
    contentScale: ContentScale = ContentScale.Crop,
    @DrawableRes errorDrawable: Int = R.drawable.no_image_icon,
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
        imageLoader = getImageLoader(LocalContext.current)
    )
}