package com.example.budgetapp.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.budgetapp.R
import com.example.budgetapp.databinding.ActivityMainBinding
import com.example.budgetapp.ui.viewModels.BudgetViewModel
import com.example.budgetapp.ui.viewModels.ProfileViewModel
import com.example.budgetapp.util.Constants
import com.example.budgetapp.util.Constants.CHANNEL_ID
import com.example.budgetapp.util.Constants.NOTIFICATION_ID
import com.example.budgetapp.util.UtilityFunctions.dateStringToMillis
import com.example.budgetapp.util.UtilityFunctions.getEndDate
import com.example.budgetapp.workmanager.NotifyUserWorker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    val profileViewModel: ProfileViewModel by viewModels()
    val budgetViewModel : BudgetViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkForProfileData()
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)
        getYesterDaysBudget()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.bottomNavBar.setupWithNavController(navHostFragment!!.findNavController())
        createNotificationChannel()


    }

    private fun getYesterDaysBudget() {
        val yesterDayinString = getEndDate(1)
        val yesterDay = dateStringToMillis(yesterDayinString)

        budgetViewModel.yesterDaysSpending(yesterDay)
        budgetViewModel.yesterDaysSpending.observe(this){yesterDaySpending ->
           yesterDaySpending?.let {


               val currentDate = Calendar.getInstance()
               val dueDate = Calendar.getInstance()

               //setting execution around 8 am
               dueDate.set(Calendar.HOUR_OF_DAY, 8)
               dueDate.set(Calendar.MINUTE, 0)
               dueDate.set(Calendar.SECOND,0)

               val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis

               val dailyWorkRequest = OneTimeWorkRequestBuilder<NotifyUserWorker>()
                   .setInitialDelay(timeDiff,TimeUnit.MILLISECONDS)
                   .setInputData(workDataOf("yesterDaySpending" to yesterDaySpending))
                   .addTag("Notification ")
                   .build()
               WorkManager.getInstance(applicationContext)
                   .enqueue(dailyWorkRequest)
           }
        }
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "BudgetApp"
            val descriptionText = "Your yesterday's spending"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply{
                description = descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkForProfileData() {
        // if no profile data -> complete profile first

        profileViewModel.profileLiveData.observe(this){
            if (it.size <1){
                navController.navigate(R.id.action_global_profileFragment)
            }
        }

        /*this.lifecycleScope.launchWhenStarted {
            profileViewModel.event.collect {event ->
                when(event){
                    is ProfileViewModel.MyEvent.navigateToProfileFragment -> {
                        navController.navigate(R.id.action_global_profileFragment)
                    }
                }

            }
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

}