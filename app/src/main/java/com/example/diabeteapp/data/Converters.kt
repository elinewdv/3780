package com.example.diabeteapp.data

import androidx.room.TypeConverter
import com.example.diabeteapp.data.api.Portion
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Converters {
    // Pour LocalDateTime
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(dateTimeFormatter)
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, dateTimeFormatter) }
    }

    // Pour Date (existant)
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // Pour Portion (existant)
    @TypeConverter
    fun fromPortions(portions: List<Portion>): String {
        return Gson().toJson(portions)
    }

    @TypeConverter
    fun toPortions(json: String): List<Portion> {
        return Gson().fromJson(json, Array<Portion>::class.java).toList()
    }
}