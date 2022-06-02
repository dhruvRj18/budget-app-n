package com.example.budgetapp.netowork


import com.example.budgetapp.entities.BudgetDB
import com.example.budgetapp.entities.Profile
import retrofit2.http.*

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
        @Body budget: BudgetDB
    ): String

    @GET("budget")
    suspend fun getBudgetEntries(): List<BudgetDB>

    @DELETE("budget/{id}")
    suspend fun deleteBudget(
        @Path("id") budget_id: String
    ): String

}