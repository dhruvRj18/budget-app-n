package com.example.budgetapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.budgetapp.R
import com.example.budgetapp.databinding.UpdateBudgetBottomSheetBinding
import com.example.budgetapp.entities.Budget
import com.example.budgetapp.ui.viewModels.BudgetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateBudgetBottomSheetFragment(
    val currentBudgetItem: Budget
) : BottomSheetDialogFragment() {

    lateinit var binding: UpdateBudgetBottomSheetBinding
    val budgetViewModel: BudgetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.update_budget_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = UpdateBudgetBottomSheetBinding.bind(view)
        binding.updateAmount.setText(currentBudgetItem.amount.toString())
        binding.updatePerpose.setText(currentBudgetItem.purpose)
        binding.updateBudgetEntry.setOnClickListener {
            val updatedAmount = binding.updateAmount.text.toString()
            val updatedPurpose = binding.updatePerpose.text.toString()
            budgetViewModel.updateBudget(
                updatedAmount.toFloat(),
                updatedPurpose,
                currentBudgetItem.id!!
            )
            dismiss()
        }


    }
}