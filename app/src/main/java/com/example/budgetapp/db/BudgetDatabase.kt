package com.example.budgetapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.budgetapp.entities.Budget

@Database(
    entities = [Budget::class],
    version = 1,
    exportSchema = true
)

abstract class BudgetDatabase : RoomDatabase() {
    abstract fun getBudgetDao():BudgetDao
}
