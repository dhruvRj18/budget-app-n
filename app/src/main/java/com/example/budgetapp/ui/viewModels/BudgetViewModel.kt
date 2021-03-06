package com.example.budgetapp.ui.viewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetapp.entities.Budget
import com.example.budgetapp.entities.BudgetDB
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
    val allBudgetEntriesFromAPI = MutableLiveData<List<BudgetDB>?>()

    var _dateRangeBudgetEntries: MutableLiveData<List<Budget>> = MutableLiveData()
    val dateRangeBudgetEntries: LiveData<List<Budget>> = _dateRangeBudgetEntries

    val totalDebit: LiveData<Float> = budgetRepository.getTotalSpending()
    val totalCredit: LiveData<Float> = budgetRepository.getTotalCredit()
    val sumOfBudget: LiveData<Float> = budgetRepository.getSumOfBudget()

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
        response.let {
            Log.d("TAG", "yesterDaysSpending: $response")
            _yesterDaysBudget.postValue(response!!)
        }
    }

    fun updateBudget(amount:Float,purpose:String,id:Int) = viewModelScope.launch {
        budgetRepository.updateBudget(amount, purpose, id)
    }

    fun updateBudgetFromAPI(budget: BudgetDB, budget_id:String) = viewModelScope.launch {
        budgetRepository.updateBudgetFromAPI(budget, budget_id)
    }

    fun deleteEntry(budgetDb:BudgetDB) = viewModelScope.launch {
        //budgetRepository.deleteEntry(budget)
        budgetRepository.deleteBudgetFromAPI(budgetDb._id?.`$oid`!!)

    }


    fun insertBudget(budget: BudgetDB) = viewModelScope.launch {
        //budgetRepository.insertBudget(budget)
        budgetRepository.insertBudgetToAPI(budget)

    }


    fun getReportBetweenDates(startDate: Long, endDate: Long) = viewModelScope.launch {
        val response = budgetRepository.budgetDao.getReportsBetweenDates(startDate, endDate)
        _dateRangeBudgetEntries.postValue(response)

    }

    fun getBudgetEntiesFromAPI()  = viewModelScope.launch {
        allBudgetEntriesFromAPI.postValue(budgetRepository.getBudgetEntriesFromAPI())
    }

}