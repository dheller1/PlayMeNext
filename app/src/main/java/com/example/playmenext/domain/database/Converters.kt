package com.example.playmenext.domain.database

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset

class Converters {
    @TypeConverter
    fun dateTimeToTimestamp(datetime: LocalDateTime?) : Long? = datetime?.toEpochSecond(ZoneOffset.UTC)

    @TypeConverter
    fun dateTimeFromTimestamp(value: Long?) : LocalDateTime?
        = value?.let{ LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) }
}