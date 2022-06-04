package com.example.budgetapp.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BudgetDB(
    val _id: Id?= null,
    val amount: Int,
    val bankName: String,
    val creditOrDebit: String,
    val date: Long,
    val purpose: String
):Parcelable{
    @Parcelize
    data class Id(
        val `$oid`: String
    ):Parcelable
}

