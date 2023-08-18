package com.noti0ns.todoformapp.extensions

import java.time.Instant
import java.time.ZoneId
import java.util.Calendar

fun Calendar.reset() = apply {
    val currentDate = Instant.now().atZone(ZoneId.systemDefault())
    set(
        currentDate.year,
        currentDate.monthValue,
        currentDate.dayOfMonth,
        currentDate.minute,
        currentDate.second
    )
}