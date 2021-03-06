package com.example.budgetapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.budgetapp.R
import com.example.budgetapp.databinding.FragmentBudgetEntryBinding
import com.example.budgetapp.entities.Budget
import com.example.budgetapp.entities.BudgetDB
import com.example.budgetapp.ui.adapters.ReportsAdapter
import com.example.budgetapp.ui.viewModels.BudgetViewModel
import com.example.budgetapp.ui.viewModels.ProfileViewModel
import com.example.budgetapp.util.UtilityFunctions.dateStringToMillis
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class BudgetEntryFragment : Fragment(R.layout.fragment_budget_entry) {

    private val viewModel: BudgetViewModel by viewModels()
    private val profileVviewModel: ProfileViewModel by viewModels()
    lateinit var binding: FragmentBudgetEntryBinding
    lateinit var bankName: String
    lateinit var debitOrCredit: String
    var currentBalance: Float = 0.0f
    lateinit var remainingBalance: String

    val args: CalenderViewFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBudgetEntryBinding.bind(view)
        activity?.title = "Enter Budget for: ${args.selectedDate}"

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
        binding.debitCreditSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
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

        binding.editAmount.addTextChangedListener { it ->
            it.let {
                val enteredAmount = it.toString()

                val rb = if (debitOrCredit.equals("Debit")) {
                    (currentBalance - enteredAmount.toFloat())

                } else {
                    (currentBalance + enteredAmount.toFloat())
                }
                remainingBalance = rb.toString()
                binding.remainingBalance.text = remainingBalance
            }
        }


       /* everyday 11:59 run this code to update current balance using work manager
        a = currentBalance + totalCredit
        newcurbalance = a - totalspending*/


        binding.submitBudgetEntry.setOnClickListener {
            val amount = binding.editAmount.text.toString()
            val purpose = binding.editPurpose.text.toString()

            val date = dateStringToMillis(args.selectedDate!!).toString()
            val revisedCurrentBalance = remainingBalance

            submitBudgetEntryToDB(
                bankName,
                debitOrCredit,
                amount,
                purpose,
                date,
                revisedCurrentBalance
            )
        }
    }

    private fun submitBudgetEntryToDB(
        bankName: String,
        debitOrCredit: String,
        amount: String,
        purpose: String,
        date: String,
        revisedCurrentBalance: String
    ) {
        var amountToInsert = amount.toFloat()
        if (debitOrCredit.equals("Debit")){
           amountToInsert = -1*amountToInsert
        }
        viewModel.insertBudget(
            BudgetDB(
                date = date.toLong(),
                bankName = bankName,
                amount =amountToInsert.toInt(),
                purpose = purpose,
                creditOrDebit = debitOrCredit,
                _id=null
            )
        )
        profileVviewModel.updateCurrentBalance(revisedBalance = revisedCurrentBalance.toFloat())
        Snackbar.make(binding.budgetEntryConstraint, "Entry added", Snackbar.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_budgetEntryFragment_to_calenderViewFragment2)
    }

    private fun setSpinnerForCreditOrDebit() {
        val creditDebitArray = ArrayList<String>()
        creditDebitArray.add("Debit")
        creditDebitArray.add("Credit")
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, creditDebitArray)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.debitCreditSpinner.adapter = arrayAdapter
    }

    private fun getProfileData() {

       /* profileVviewModel.profileLiveData.observe(viewLifecycleOwner) {
            val bankNames = ArrayList<String>()
            bankNames.add(it[0].bankName)
            currentBalance = it[0].currentBalance
            binding.remainingBalance.text = it[0].currentBalance.toString()
            val arrayAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bankNames)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.bankSpinner.adapter = arrayAdapter
        }*/
        profileVviewModel.getProfileDataFromAPI()
        profileVviewModel.profileLiveDataAPI.observe(viewLifecycleOwner){
            val bankNames = ArrayList<String>()
            bankNames.add(it[0].bankName)
            currentBalance = it[0].currentBalance
            binding.remainingBalance.text = it[0].currentBalance.toString()
            val arrayAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bankNames)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.bankSpinner.adapter = arrayAdapter
        }
    }
}