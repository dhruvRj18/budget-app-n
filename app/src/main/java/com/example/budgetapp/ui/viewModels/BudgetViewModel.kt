package com.example.budgetapp.ui.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.budgetapp.repositories.BudgetRepository
import javax.inject.Inject

class BudgetViewModel @ViewModelInject constructor(
    val budgetRepository: BudgetRepository
) :ViewModel(){
}