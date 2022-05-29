package com.example.budgetapp.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetapp.entities.Profile
import com.example.budgetapp.repositories.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val profileRepository: ProfileRepository
) : ViewModel() {


    //val profileLiveData:LiveData<List<Profile>> = profileRepository.getProfile()
    var profileLiveDataAPI = MutableLiveData<List<Profile>>()


    fun insertProfileData(profile: Profile) = viewModelScope.launch {
        profileRepository.insertProfileData(profile )
        profileRepository.insertProfileDataToApi(profile)
    }

    fun updateCurrentBalance(revisedBalance:Float) = viewModelScope.launch {
        profileRepository.updateCurrentBalance(revisedBalance)
    }

    fun getProfileDataFromAPI() = viewModelScope.launch {
        profileLiveDataAPI.postValue(profileRepository.getProfileDataFromAPI())
    }



}