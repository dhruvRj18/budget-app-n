package com.example.budgetapp.db

import androidx.room.*
import com.example.budgetapp.entities.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileData(profile: Profile)

    @Query("SELECT * from profile_table")
    suspend fun getProfileData():List<Profile>

    @Query("UPDATE profile_table SET currentBalance = :revisedBalance")
    suspend fun updateCurrentBalance(revisedBalance:Float)

}