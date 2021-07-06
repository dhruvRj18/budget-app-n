package com.example.budgetapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgetapp.entities.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Dao
interface ProfileDao {

   /* @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileData(profile: Profile)
*/
    @Query("SELECT * from profile_table")
    suspend fun getProfileData():List<Profile>

}