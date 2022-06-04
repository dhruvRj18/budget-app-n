package com.example.budgetapp.repositories

import com.example.budgetapp.db.BudgetDao
import com.example.budgetapp.entities.Budget
import com.example.budgetapp.entities.BudgetDB
import com.example.budgetapp.netowork.Api
import javax.inject.Inject

class BudgetRepository @Inject constructor(
    val budgetDao: BudgetDao,
    val api: Api
) {
    suspend fun insertBudget(budget: Budget) = budgetDao.insertBudget(budget)

     fun getAllBudgetEntries() = budgetDao.getAllData()

    suspend fun getBudgetEntriesBetweenDates(startDate: Long, endDate: Long) =
        budgetDao.getReportsBetweenDates(
            startDate, endDate
        )

    suspend fun updateBudget(amount:Float,purpose:String, id:Int) = budgetDao.updateBudget(amount, purpose, id)


     fun getTotalSpending() = budgetDao.getTotalSpending()

     fun getTotalCredit() = budgetDao.getTotalCredit()

     fun getSumOfBudget() = budgetDao.getSumOfBudget()

    suspend fun getYesterDaySpending(yesterDay:Long) = budgetDao.getYesterDaySpending(yesterDay)
    suspend fun getYesterDayBudget(yesterDay:Long) = budgetDao.getYesterDayBudget(yesterDay)
    suspend fun deleteEntry(budget: Budget) = budgetDao.deleteEntry(budget)


    suspend fun insertBudgetToAPI(budget: BudgetDB) = api.insertBudgetEntry(budget)

    suspend fun getBudgetEntriesFromAPI() = api.getBudgetEntries()

    suspend fun deleteBudgetFromAPI(budget_id:String) = api.deleteBudget(budget_id)

    suspend fun updateBudgetFromAPI(budget: BudgetDB, budget_id: String) = api.updateBudget(budget, budget_id)

}