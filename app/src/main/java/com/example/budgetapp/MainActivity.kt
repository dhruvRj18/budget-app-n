package com.example.budgetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.budgetapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val calView = CalenderViewFragment()

        setCurrentFragment(calView)
        binding.bottomNavBar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.editBudget -> setCurrentFragment(calView)
            }
            true
        }



    }

    private fun setCurrentFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutMain,fragment)
            commit()
        }
    }
}