package com.example.budgetapp.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_table")
data class Profile(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val name: String,
    val email:String,
    val profileImage:Bitmap?=null,
    val bankName:String,
    val currentBalance:Float,
    val primaryBank:Boolean
) {
}