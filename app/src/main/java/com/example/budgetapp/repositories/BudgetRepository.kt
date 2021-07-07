package com.example.budgetapp.repositories

import com.example.budgetapp.db.BudgetDao
import com.example.budgetapp.entities.Budget
import javax.inject.Inject

class BudgetRepository @Inject constructor(
    val budgetDao: BudgetDao
) {
    suspend fun insertBudget(budget: Budget)= budgetDao.insertBudget(budget)
}