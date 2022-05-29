package com.example.budgetapp.netowork


import com.example.budgetapp.entities.Profile
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {


    @POST("user")
    suspend fun insertProfileData(
        @Body profile: Profile
    ): Profile

    @GET("user")
    suspend fun getProfileData(): List<Profile>
}