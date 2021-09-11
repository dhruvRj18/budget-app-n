package com.example.budgetapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "budget"
)
data class Budget(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val date:String,
    val bankName:String,
    val amount:Float,
    val purpose:String,
    val creditOrDebit:String
) {
}