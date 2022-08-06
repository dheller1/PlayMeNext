package com.example.playmenext.domain.database

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?) : LocalDate? = value?.let{ LocalDate.ofEpochDay(it) }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?) : Long? = date?.toEpochDay()

    @TypeConverter
    fun dateTimeToTimestamp(datetime: LocalDateTime?) : Long? = datetime?.toEpochSecond(ZoneOffset.UTC)

    @TypeConverter
    fun dateTimeFromTimestamp(value: Long?) : LocalDateTime?
        = value?.let{ LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) }
}