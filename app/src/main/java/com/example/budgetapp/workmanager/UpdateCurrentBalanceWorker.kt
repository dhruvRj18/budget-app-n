package com.example.budgetapp.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class UpdateCurrentBalanceWorker(context: Context, params:WorkerParameters) : Worker(context,params) {

    override fun doWork(): Result {
        updateCurrentBalance(inputData.getFloat("newCurrentBalance",0f))
        return Result.success()
    }

    private fun updateCurrentBalance(newCurrentBalance: Float) {

    }
}