package com.example.budgetapp.db

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun fromDate(date: Date): String {
        return date.toString()
    }
}