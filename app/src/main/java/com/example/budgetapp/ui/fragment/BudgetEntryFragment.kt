package com.example.budgetapp.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.budgetapp.R
import com.example.budgetapp.databinding.FragmentBudgetEntryBinding
import com.example.budgetapp.entities.Budget
import com.example.budgetapp.ui.viewModels.BudgetViewModel
import com.example.budgetapp.ui.viewModels.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class BudgetEntryFragment : Fragment(R.layout.fragment_budget_entry) {

    private val viewModel: BudgetViewModel by viewModels()
    private val profileVviewModel: ProfileViewModel by viewModels()
    lateinit var binding: FragmentBudgetEntryBinding
    lateinit var bankName:String
    lateinit var debitOrCredit:String

    val args: CalenderViewFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBudgetEntryBinding.bind(view)
        activity?.title = "Enter Budget for: ${args.selectedDate}"
        Snackbar.make(binding.budgetEntryConstraint, "" + args.selectedDate, Snackbar.LENGTH_SHORT)
            .show()
        getProfileData()

        setSpinnerForCreditOrDebit()

        binding.bankSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                bankName = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                bankName = "NONE"
            }
        }
        binding.debitCreditSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                debitOrCredit = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                debitOrCredit = "Debit"
            }
        }
        binding.submitBudgetEntry.setOnClickListener {
            val amount = binding.editAmount.text.toString()
            val purpose = binding.editPurpose.text.toString()
            val cal = Calendar.getInstance()
            val date = cal.timeInMillis.toString()
            submitBudgetEntryToDB(bankName,debitOrCredit,amount,purpose,date)
        }
    }

    private fun submitBudgetEntryToDB(bankName: String, debitOrCredit: String, amount: String, purpose: String, date: String) {
        viewModel.insertBudget(Budget(date = date,bankName = bankName,amount = amount.toFloat(),purpose = purpose,creditOrDebit = debitOrCredit))
        Snackbar.make(binding.budgetEntryConstraint,"Entry added",Snackbar.LENGTH_SHORT).show()
    }


    private fun setSpinnerForCreditOrDebit() {
        val creditDebitArray = ArrayList<String> ()
        creditDebitArray.add("Credit")
        creditDebitArray.add("Debit")
        val arrayAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,creditDebitArray)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.debitCreditSpinner.adapter = arrayAdapter
    }

    private fun getProfileData() {
        profileVviewModel.getProfile()
        profileVviewModel.profileLiveData.observe(viewLifecycleOwner) {
            val bankNames = ArrayList<String>()
            bankNames.add(it.bankName)
            val arrayAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bankNames)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.bankSpinner.adapter = arrayAdapter
        }
    }
}