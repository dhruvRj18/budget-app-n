package com.example.budgetapp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.budgetapp.R
import com.example.budgetapp.databinding.FragmentBudgetEntryBinding
import com.example.budgetapp.ui.viewModels.BudgetViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BudgetEntryFragment : Fragment(R.layout.fragment_budget_entry) {

    private val viewModel: BudgetViewModel by viewModels()
    lateinit var binding: FragmentBudgetEntryBinding

    val args: CalenderViewFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBudgetEntryBinding.bind(view)
        activity?.title = "Enter Budget for: ${args.selectedDate}"
        Snackbar.make(binding.budgetEntryConstraint, "" + args.selectedDate, Snackbar.LENGTH_SHORT)
            .show()
        viewModel.callToast()
    }
}