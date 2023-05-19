package com.flexberry.androidodataofflinesample.data.local.utils

import androidx.room.TypeConverter
import java.sql.Date
import java.sql.Timestamp

class Converters {
    @TypeConverter
    fun fromLongToTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(it) }
    }

    @TypeConverter
    fun fromTimestampToLong(date: Timestamp?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromLongToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun fromDateToLong(date: Date?): Long? {
        return date?.time?.toLong()
    }
}