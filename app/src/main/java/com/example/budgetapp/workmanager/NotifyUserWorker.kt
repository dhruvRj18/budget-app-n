package com.example.budgetapp.workmanager

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.budgetapp.R
import com.example.budgetapp.ui.MainActivity
import com.example.budgetapp.util.Constants
import java.util.*
import java.util.concurrent.TimeUnit

class NotifyUserWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        sendNotification(inputData.getFloat("yesterDaySpending", 0f))

        return Result.success()
    }

    private fun sendNotification(yesterDaySpending: Float) {
        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.reportsFragment)
            .setArguments(bundleOf("yesterDaySpending" to yesterDaySpending))
            .createPendingIntent()

        val builder = NotificationCompat.Builder(applicationContext, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_debit)
            .setContentTitle("Budget App")
            .setContentText("Your Yesterday's spending is £$yesterDaySpending")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(Constants.NOTIFICATION_ID, builder.build())
        }

    }
}