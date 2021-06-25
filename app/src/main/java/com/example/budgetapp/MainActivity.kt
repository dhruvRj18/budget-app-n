package com.example.budgetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.budgetapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val profileFragment = ProfileFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val calView = CalenderViewFragment()
        val reportFragment = ReportsFragment()
        val graphFragment = GraphsFragment()

        setCurrentFragment(calView)
        binding.bottomNavBar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.editBudget -> setCurrentFragment(calView)
                R.id.reports -> setCurrentFragment(reportFragment)
                R.id.graphs -> setCurrentFragment(graphFragment)
            }
            true
        }

    }
    public fun setActinBarTitle(title:String){
        supportActionBar?.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menuInflater.inflate(R.menu.option_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.profile -> setCurrentFragment(profileFragment)
        }
        return true
    }

    private fun setCurrentFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutMain,fragment)
            commit()
        }
    }
}