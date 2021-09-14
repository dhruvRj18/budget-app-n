package com.example.budgetapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.budgetapp.entities.Budget

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBudget(budget: Budget)

    @Query("UPDATE budget SET amount = :amount, purpose = :purpose WHERE id = :id")
    suspend fun updateBudget(amount:Float,purpose:String,id:Int)

    @Query("SELECT * FROM budget ORDER BY id ASC")
     fun getAllData(): LiveData<List<Budget>>

    @Query("SELECT SUM(amount) FROM budget WHERE creditOrDebit = 'Debit'")
     fun getTotalSpending():LiveData<Float>

    @Query("SELECT SUM(amount) FROM budget WHERE creditOrDebit = 'Credit'")
     fun getTotalCredit():LiveData<Float>

    @Query("SELECT SUM(amount) FROM budget")
     fun getTotalTransactionValue():LiveData<Float>

    @Query("SELECT * FROM budget WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getReportsBetweenDates(startDate:Long, endDate:Long):List<Budget>

    @Query("SELECT SUM(amount) FROM budget WHERE date = :date AND creditOrDebit = 'Debit'")
    suspend fun getYesterDaySpending(date:Long):Float?


    @Query("SELECT * FROM BUDGET WHERE date = :yesterDay")
    suspend fun getYesterDayBudget(yesterDay:Long):List<Budget>

    @Delete
    suspend fun deleteEntry(budget: Budget)



    //make function using total credit and total spending to calculate latest currentBalance
}