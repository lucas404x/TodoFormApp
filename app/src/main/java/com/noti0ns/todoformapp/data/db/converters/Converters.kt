package com.noti0ns.todoformapp.data.db.converters

import androidx.room.TypeConverter
import java.time.Clock
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? = value?.let {
        LocalDateTime.parse(value).atZone(Clock.systemDefaultZone().zone).toLocalDateTime()
    }

    @TypeConverter
    fun localDateTimeToString(localDateTime: LocalDateTime?): String? = localDateTime?.toString()
}