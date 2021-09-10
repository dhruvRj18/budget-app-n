package com.example.budgetapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgetapp.R
import com.example.budgetapp.databinding.FragmentReportsBinding
import com.example.budgetapp.entities.Budget
import com.example.budgetapp.ui.adapters.ReportsAdapter
import com.example.budgetapp.ui.viewModels.BudgetViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportsFragment : Fragment(R.layout.fragment_reports) {
    lateinit var binding: FragmentReportsBinding
    val budgetViewModel: BudgetViewModel by viewModels()

    var reportsAdapter: ReportsAdapter = ReportsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReportsBinding.bind(view)
        setHasOptionsMenu(true)
        activity?.title = "Spending Reports"


        budgetViewModel.getAllEntries()
        budgetViewModel.allBudgetEntries.observe(viewLifecycleOwner) {
            reportsAdapter.differ.submitList(it)
        }

        binding.rcvReports.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reportsAdapter

        }
    }
}
