package com.example.budgetapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.budgetapp.databinding.FragmentGraphsBinding

class GraphsFragment: Fragment(R.layout.fragment_graphs) {

    lateinit var binding: FragmentGraphsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGraphsBinding.bind(view)
        activity?.title = "Graphical Reports"

    }
}