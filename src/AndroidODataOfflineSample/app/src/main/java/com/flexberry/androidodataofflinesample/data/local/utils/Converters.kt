package com.flexberry.androidodataofflinesample.data.local.utils

import androidx.room.TypeConverter
import java.sql.Date
import java.sql.Timestamp
import java.util.UUID

/**
 * Конвертер типов в Room
 */
class Converters {
    /**
     * Конвертация [Long] в [Timestamp].
     *
     * @param long Объект типа [Long].
     * @return Объект типа [Timestamp].
     */
    @TypeConverter
    fun fromLongToTimestamp(long: Long?): Timestamp? {
        return long?.let { Timestamp(it) }
    }

    /**
     * Конвертация  [Timestamp] в [Long].
     *
     * @param date Объект типа [Timestamp].
     * @return Объект типа [Long].
     */
    @TypeConverter
    fun fromTimestampToLong(date: Timestamp?): Long? {
        return date?.time
    }

    /**
     * Конвертация [Long] в [Date].
     *
     * @param long Объект типа [Long].
     * @return Объект типа [Date].
     */
    @TypeConverter
    fun fromLongToDate(long: Long?): Date? {
        return long?.let { Date(it) }
    }

    /**
     * Конвертация  [Date] в [Long].
     *
     * @param date Объект типа [Long].
     * @return Объект типа [Date].
     */
    @TypeConverter
    fun fromDateToLong(date: Date?): Long? {
        return date?.time
    }

    /**
     * Конвертация [UUID] в [String].
     *
     * @param uuid Объект типа [UUID].
     * @return Объект типа [String].
     */
    @TypeConverter
    fun fromUUIDtoString(uuid: UUID?): String {
        return uuid?.toString() ?: String()
    }

    /**
     * Конвертация [String] в [UUID].
     *
     * @param string Объект типа [String].
     * @return Объект типа [UUID].
     */
    @TypeConverter
    fun fromStringToUUID(string: String?): UUID? {
        return if (!string.isNullOrEmpty()) UUID.fromString(string) else null
    }
}