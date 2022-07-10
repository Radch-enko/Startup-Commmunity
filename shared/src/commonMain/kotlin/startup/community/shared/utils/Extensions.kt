package startup.community.shared.utils

import io.github.aakira.napier.Napier
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
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

fun String.toFullDate(): String {
    return try {
        val localDateTime = this.substringBefore(".").toLocalDateTime()

        val monthDisplay = localDateTime.month.number.let {
            if (it < 10) {
                "0$it"
            } else {
                it.toString()
            }
        }

        "${localDateTime.dayOfMonth}.$monthDisplay.${localDateTime.year}"
    } catch (e: Exception) {
        Napier.e("CommentDateCast", e)
        "Incorrect date"
    }
}

fun LocalDate.toFullDate(): String {
    return try {
        val monthDisplay = this.month.number.let {
            if (it < 10) {
                "0$it"
            } else {
                it.toString()
            }
        }

        "${this.dayOfMonth}.$monthDisplay.${this.year}"
    } catch (e: Exception) {
        Napier.e("CommentDateCast", e)
        "Incorrect date"
    }
}