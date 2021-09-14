package com.example.budgetapp.ui.viewModels

import android.util.Log
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
) : ViewModel() {


    val allBudgetEntries: LiveData<List<Budget>> = budgetRepository.getAllBudgetEntries()

    var _dateRangeBudgetEntries: MutableLiveData<List<Budget>> = MutableLiveData()
    val dateRangeBudgetEntries: LiveData<List<Budget>> = _dateRangeBudgetEntries

    val totalDebit: LiveData<Float> = budgetRepository.getTotalSpending()
    val totalCredit: LiveData<Float> = budgetRepository.getTotalCredit()
    val totalTransaction: LiveData<Float> = budgetRepository.getTotalTransaction()

    var _yesterDaysSpending: MutableLiveData<Float> = MutableLiveData()
    val yesterDaysSpending: LiveData<Float> = _yesterDaysSpending

    var _yesterDaysBudget: MutableLiveData<List<Budget>> = MutableLiveData()
    val yesterDaysBudget: LiveData<List<Budget>> = _yesterDaysBudget


    fun yesterDaysSpending(yesterDay: Long) = viewModelScope.launch {
        val response = budgetRepository.getYesterDaySpending(yesterDay)
        response?.let {
            Log.d("TAG", "yesterDaysSpending: $response")
            _yesterDaysSpending.postValue(response!!)
        }
    }

    fun yesterDaysBudget(yesterDay: Long) = viewModelScope.launch {
        val response = budgetRepository.getYesterDayBudget(yesterDay)
        response?.let {
            Log.d("TAG", "yesterDaysSpending: $response")
            _yesterDaysBudget.postValue(response!!)
        }
    }

    fun deleteEntry(budget: Budget) = viewModelScope.launch {
        budgetRepository.deleteEntry(budget)

    }


    fun insertBudget(budget: Budget) = viewModelScope.launch {
        budgetRepository.insertBudget(budget)

    }


    fun getReportBetweenDates(startDate: Long, endDate: Long) = viewModelScope.launch {
        val response = budgetRepository.budgetDao.getReportsBetweenDates(startDate, endDate)
        _dateRangeBudgetEntries.postValue(response)

    }


}