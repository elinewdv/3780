package com.example.diabeteapp.data


import androidx.room.TypeConverter
import com.example.diabeteapp.data.api.Portion
import com.google.gson.Gson

import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
class PortionConverter {
    @TypeConverter
    fun fromPortions(portions: List<Portion>): String {
        return Gson().toJson(portions)
    }
}