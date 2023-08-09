package com.noti0ns.todoformapp.data.db.converters

import androidx.room.TypeConverter
import java.time.Instant

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? =
        if (value == null) null else Instant.ofEpochMilli(value)

    @TypeConverter
    fun instantToTimestamp(instant: Instant?): Long? = instant?.toEpochMilli()
}