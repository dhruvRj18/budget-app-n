package com.example.budgetapp.repositories

import com.example.budgetapp.db.ProfileDao
import com.example.budgetapp.entities.Profile
import kotlinx.coroutines.flow.flow
import java.util.concurrent.Flow
import javax.inject.Inject

class ProfileRepository @Inject constructor(val profileDao: ProfileDao) {


    suspend fun insertProfileData(profile: Profile) = profileDao.insertProfileData(profile)

    suspend fun getProfile() = profileDao.getProfileData()

    suspend fun updateCurrentBalance(revidesBalance:Float) = profileDao.updateCurrentBalance(revidesBalance)

}