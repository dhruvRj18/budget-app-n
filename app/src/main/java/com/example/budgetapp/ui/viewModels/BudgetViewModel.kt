package com.example.budgetapp.ui.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.budgetapp.repositories.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    val budgetRepository: BudgetRepository
) :ViewModel(){
    fun callToast()= "test"
}