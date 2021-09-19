package com.example.budgetapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.budgetapp.R
import com.example.budgetapp.databinding.StatasticsBottomSheetBinding
import com.example.budgetapp.ui.viewModels.BudgetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {

   lateinit var binding: StatasticsBottomSheetBinding

   val budgetViewModel : BudgetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.statastics_bottom_sheet,container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = StatasticsBottomSheetBinding.bind(view)


        budgetViewModel.totalCredit.observe(viewLifecycleOwner){
            binding.totalCredit.text =it.toString()
        }
        budgetViewModel.totalDebit.observe(viewLifecycleOwner){
            binding.totalSpending.text =it.toString()
        }
    }
}