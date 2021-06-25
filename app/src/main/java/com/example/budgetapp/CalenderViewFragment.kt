package com.example.budgetapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.budgetapp.databinding.FragmentCalenderViewBinding
import com.google.android.material.snackbar.Snackbar

class CalenderViewFragment : Fragment(R.layout.fragment_calender_view) {

    lateinit var binding : FragmentCalenderViewBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalenderViewBinding.bind(view)
        setHasOptionsMenu(true)
        activity?.title = "Enter Your Budget"
        binding.textView.setOnClickListener {

        }

        binding.calView.setOnDateChangeListener { view, year, month, dayOfMonth ->



            Snackbar.make(
                binding.calViewConstraint,
                "Data: ${dayOfMonth} / ${month + 1} / ${year}",
                Snackbar.LENGTH_SHORT
            ).show()


        }
    }

}