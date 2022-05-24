package com.multi.producthunt.android.ui

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
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

    return inputStream?.readBytes()
}