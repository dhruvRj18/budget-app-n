package com.example.budgetapp.ui.viewModels

import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetapp.entities.Budget
import com.example.budgetapp.repositories.BudgetRepository
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    val budgetRepository: BudgetRepository
) :ViewModel(){

    var _allBudgetEntries : MutableLiveData<List<Budget>> = MutableLiveData()
    val allBudgetEntries: LiveData<List<Budget>> = _allBudgetEntries

    var _dateRangeBudgetEntries : MutableLiveData<List<Budget>> = MutableLiveData()
    val dateRangeBudgetEntries:LiveData<List<Budget>> = _dateRangeBudgetEntries

    fun insertBudget(budget: Budget) = viewModelScope.launch {
        budgetRepository.insertBudget(budget)

    }

    fun getAllEntries() = viewModelScope.launch {
        val response = budgetRepository.getAllBudgetEntries()
        _allBudgetEntries.postValue(response)
    }

    fun getReportBetweenDates(startDate:Long,endDate:Long) = viewModelScope.launch {
        val response = budgetRepository.budgetDao.getReportsBetweenDates(startDate, endDate)
        _dateRangeBudgetEntries.postValue(response)
    }


}