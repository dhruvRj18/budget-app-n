package com.example.budgetapp.repositories

import com.example.budgetapp.db.ProfileDao
import com.example.budgetapp.entities.Profile
import com.example.budgetapp.netowork.Api
import kotlinx.coroutines.flow.flow
import java.util.concurrent.Flow
import javax.inject.Inject

class ProfileRepository @Inject constructor(val profileDao: ProfileDao, val api: Api) {


    suspend fun insertProfileData(profile: Profile) = profileDao.insertProfileData(profile)

     fun getProfile() = profileDao.getProfileData()

    suspend fun updateCurrentBalance(revidesBalance:Float) = profileDao.updateCurrentBalance(revidesBalance)

    suspend fun insertProfileDataToApi(profile: Profile) = api.insertProfileData(profile)

    suspend fun getProfileDataFromAPI() = api.getProfileData()

}