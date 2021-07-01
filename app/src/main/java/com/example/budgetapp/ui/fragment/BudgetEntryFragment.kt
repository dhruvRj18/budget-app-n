package com.example.budgetapp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.budgetapp.CalenderViewFragmentArgs
import com.example.budgetapp.R
import com.example.budgetapp.databinding.FragmentBudgetEntryBinding
import com.google.android.material.snackbar.Snackbar

class BudgetEntryFragment: Fragment(R.layout.fragment_budget_entry) {
    lateinit var binding: FragmentBudgetEntryBinding
    val args: CalenderViewFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBudgetEntryBinding.bind(view)
        activity?.title = "Enter Budget for: ${args.selectedDate}"
        Snackbar.make(binding.budgetEntryConstraint,""+args.selectedDate,Snackbar.LENGTH_SHORT).show()
    }
}