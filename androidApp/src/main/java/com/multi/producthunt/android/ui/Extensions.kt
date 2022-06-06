package com.multi.producthunt.android.ui

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.webkit.URLUtil
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.net.toUri
import coil.request.ImageRequest
import kotlinx.datetime.LocalDate
import java.io.InputStream

fun Uri.toImageBitmap(context: Context): ImageBitmap {
    val bitmap = this.let {
        if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images
                .Media.getBitmap(context.contentResolver, it)

        } else {
            val source = ImageDecoder
                .createSource(context.contentResolver, it)
            ImageDecoder.decodeBitmap(source)
        }
    }
    return bitmap.asImageBitmap()
}

fun Uri.toByteArray(context: Context): ByteArray? {
    val inputStream: InputStream? =
        context.contentResolver.openInputStream(this)


    val imageRequest = ImageRequest.Builder(context)
        .data(this)
        .build()

    return inputStream?.readBytes()
}

fun String.uploadMedia(context: Context): String? {
    return if (URLUtil.isHttpUrl(this) || URLUtil.isHttpsUrl(this)) {
        this
    } else {
        this.toUri().toByteArray(context)?.toBase64()
    }
}

fun ByteArray.toBase64(): String {
    return "data:image/jpeg;base64," + Base64.encodeToString(this, Base64.DEFAULT)
}

fun LocalDate.toTitle(): String {
    return "${this.dayOfMonth} ${this.month.name} ${this.year}"
}