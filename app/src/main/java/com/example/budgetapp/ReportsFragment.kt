package com.example.budgetapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.budgetapp.databinding.FragmentReportsBinding

class ReportsFragment: Fragment(R.layout.fragment_reports) {
    lateinit var binding: FragmentReportsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReportsBinding.bind(view)
        setHasOptionsMenu(true)
        activity?.title = "Spending Reports"
    }
}