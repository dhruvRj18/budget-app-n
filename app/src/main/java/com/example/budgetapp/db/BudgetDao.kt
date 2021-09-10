package com.example.budgetapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgetapp.entities.Budget

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBudget(budget: Budget)

    @Query("SELECT * FROM budget ORDER BY id DESC")
    suspend fun getAllData(): List<Budget>

    @Query("SELECT SUM(amount) FROM budget WHERE creditOrDebit = 'Debit'")
    suspend fun getTotalSpending():Float
    @Query("SELECT SUM(amount) FROM budget WHERE creditOrDebit = 'Credit'")
    suspend fun getTotalCredit():Float

    @Query("SELECT SUM(amount) FROM budget")
    suspend fun getTotalTransactionValue():Float

    @Query("SELECT * FROM budget WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getReportsBetweenDates(startDate:String, endDate:String):List<Budget>

}