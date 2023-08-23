package com.noti0ns.todoformapp.extensions

import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar

fun Calendar.reset() = apply {
    val currentDate = ZonedDateTime.now()
    set(
        currentDate.year,
        currentDate.monthValue - 1,
        currentDate.dayOfMonth,
        currentDate.minute,
        currentDate.second
    )
}

fun LocalDateTime?.renderFullDateTime(): String? = this?.format(
    DateTimeFormatter.ofLocalizedDateTime(
        FormatStyle.MEDIUM,
        FormatStyle.SHORT
    )
)

fun LocalDateTime?.renderShortDate(): String? =
    this?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))