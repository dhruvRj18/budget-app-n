package com.example.budgetapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.budgetapp.databinding.FragmentBudgetEntryBinding

class BudgetEntryFragment: Fragment(R.layout.fragment_budget_entry) {
    lateinit var binding: FragmentBudgetEntryBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBudgetEntryBinding.bind(view)
    }
}