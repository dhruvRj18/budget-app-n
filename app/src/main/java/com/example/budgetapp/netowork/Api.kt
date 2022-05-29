package com.example.budgetapp.netowork


import com.example.budgetapp.entities.Budget
import com.example.budgetapp.entities.Profile
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {


//User end points
    @POST("user")
    suspend fun insertProfileData(
        @Body profile: Profile
    ): Profile

    @GET("user")
    suspend fun getProfileData(): List<Profile>

//Budget end points

    @POST("budget")
    suspend fun insertBudgetEntry(
        @Body budget: Budget
    ):Budget



}