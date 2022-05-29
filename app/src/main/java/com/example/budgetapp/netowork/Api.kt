package com.example.budgetapp.netowork


import com.example.budgetapp.entities.Budget
import com.example.budgetapp.entities.Profile
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {


    @POST("user")
    suspend fun insertProfileData(
        @Body profile: Profile
    ): Budget
}