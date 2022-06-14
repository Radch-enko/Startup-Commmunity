package com.multi.producthunt.utils

import io.github.aakira.napier.Napier
import kotlinx.datetime.toLocalDateTime

fun String.toCommentDate(): String? {
    return try {
        val localDateTime = this.substringBefore(".").toLocalDateTime()

        "${localDateTime.dayOfMonth} ${localDateTime.month.name[0]}${
            localDateTime.month.name.substring(
                1
            ).lowercase()
        }"
    } catch (e: Exception) {
        Napier.e("CommentDateCast", e)
        null
    }
}

fun String.toFullDate(): String? {
    return try {
        val localDateTime = this.substringBefore(".").toLocalDateTime()

        "${localDateTime.dayOfMonth} ${localDateTime.month.name[0]}${
            localDateTime.month.name.substring(
                1
            ).lowercase()
        } ${localDateTime.year}"
    } catch (e: Exception) {
        Napier.e("CommentDateCast", e)
        null
    }
}