package com.example.budgetapp.ui.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgetapp.R
import com.example.budgetapp.databinding.FragmentReportsBinding
import com.example.budgetapp.databinding.StatasticsBottomSheetBinding
import com.example.budgetapp.entities.Budget
import com.example.budgetapp.ui.adapters.ReportsAdapter
import com.example.budgetapp.ui.viewModels.BudgetViewModel
import com.example.budgetapp.util.UtilityFunctions
import com.example.budgetapp.util.UtilityFunctions.dateMillisToString
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ReportsFragment : Fragment(R.layout.fragment_reports) {
    lateinit var binding: FragmentReportsBinding
    val budgetViewModel: BudgetViewModel by viewModels()

    var reportsAdapter: ReportsAdapter = ReportsAdapter()
    val dateRangeArray = arrayOf("Select Date Range", "1 Week", "1 Month", "6 Month","Show All")
    lateinit var startDate: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReportsBinding.bind(view)
        setHasOptionsMenu(true)
        activity?.title = "Spending Reports"
        setSpinnerValues()
        initializeRecyclerView()

        startDate = setStartDate()
        binding.totalSpending.setOnClickListener {
            val bottomSheet = BottomSheetFragment()
            bottomSheet.show(requireActivity().supportFragmentManager,"BottomSheet")
        }

        binding.dateRangeReportSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (parent?.getItemAtPosition(position)) {

                        "1 Week" -> getReportBetweenDates(startDate,getEndDate( 7))
                        "1 Month" -> getReportBetweenDates(startDate,getEndDate( 30))
                        "6 Month" -> getReportBetweenDates(startDate,getEndDate( 180))
                        "Show All" -> getAllEntries()
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

        getAllEntries()


    }

    private fun getAllEntries() {
        budgetViewModel.getAllEntries()
        budgetViewModel.allBudgetEntries.observe(viewLifecycleOwner) {
            reportsAdapter.differ.submitList(it)
        }
    }

    private fun getReportBetweenDates(startDate: String, endDate: String) {
        val start = UtilityFunctions.dateStringToMillis(endDate)
        val end = UtilityFunctions.dateStringToMillis(startDate)
        Log.d("TAG", "getReportBetweenDates:$start : $startDate \n $end : $endDate ")

        budgetViewModel.getReportBetweenDates(start,end)
        budgetViewModel.dateRangeBudgetEntries.observe(viewLifecycleOwner){
            reportsAdapter.differ.submitList(it)
        }

    }

    private fun getEndDate(daysToCount: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -daysToCount)
        return dateMillisToString(cal.timeInMillis)
    }

    private fun setStartDate(): String {
        val dateInMillis = Calendar.getInstance().timeInMillis
        return dateMillisToString(dateInMillis)

    }

    private fun initializeRecyclerView() {
        binding.rcvReports.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reportsAdapter

        }
    }

    private fun setSpinnerValues() {

        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dateRangeArray)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.dateRangeReportSpinner.adapter = arrayAdapter
    }
}
