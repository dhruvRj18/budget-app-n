package com.example.budgetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.budgetapp.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.title = "My Profile"
    }
}