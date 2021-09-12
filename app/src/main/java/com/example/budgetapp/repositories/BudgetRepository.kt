package com.example.budgetapp.repositories

import com.example.budgetapp.db.BudgetDao
import com.example.budgetapp.entities.Budget
import javax.inject.Inject

class BudgetRepository @Inject constructor(
    val budgetDao: BudgetDao
) {
    suspend fun insertBudget(budget: Budget) = budgetDao.insertBudget(budget)

     fun getAllBudgetEntries() = budgetDao.getAllData()

    suspend fun getBudgetEntriesBetweenDates(startDate: Long, endDate: Long) =
        budgetDao.getReportsBetweenDates(
            startDate, endDate
        )

     fun getTotalSpending() = budgetDao.getTotalSpending()

     fun getTotalCredit() = budgetDao.getTotalCredit()

     fun getTotalTransaction() = budgetDao.getTotalTransactionValue()

    suspend fun getYesterDaySpending(yesterDay:Long) = budgetDao.getYesterDaySpending(yesterDay)
    suspend fun deleteEntry(budget: Budget) = budgetDao.deleteEntry(budget)

}