package com.example.budgetapp.ui.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetapp.entities.Profile
import com.example.budgetapp.repositories.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val profileRepository: ProfileRepository
) : ViewModel() {


    val profileLiveData:LiveData<List<Profile>> = profileRepository.getProfile()

    fun insertProfileData(profile: Profile) = viewModelScope.launch {
        profileRepository.insertProfileData(profile )
    }

    fun updateCurrentBalance(revisedBalance:Float) = viewModelScope.launch {
        profileRepository.updateCurrentBalance(revisedBalance)
    }
}