package com.example.budgetapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.budgetapp.entities.Budget
import com.example.budgetapp.entities.Profile

@Database(
    entities = [Budget::class, Profile::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class BudgetDatabase : RoomDatabase() {
    abstract fun getBudgetDao():BudgetDao
    abstract fun getProfileDao():ProfileDao

}
