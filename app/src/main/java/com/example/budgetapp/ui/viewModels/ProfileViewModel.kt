package com.example.budgetapp.ui.viewModels

import androidx.hilt.lifecycle.ViewModelInject
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

    var profileLiveData:MutableLiveData<Profile> = MutableLiveData<Profile>()

    private val eventChanel = kotlinx.coroutines.channels.Channel<MyEvent>()
    val event = eventChanel.receiveAsFlow()

    fun getProfile() = viewModelScope.launch {
        val profileData = profileRepository.getProfile()
        if (profileData.size>0){
            profileLiveData.postValue(profileData[0])
        }else{
            eventChanel.send(MyEvent.navigateToProfileFragment)
        }
    }

    fun insertProfileData(profile: Profile) = viewModelScope.launch {
        profileRepository.insertProfileData(profile )
    }

    fun updateCurrentBalance(revisedBalance:Float) = viewModelScope.launch {
        profileRepository.updateCurrentBalance(revisedBalance)
    }


    sealed class MyEvent{
        object navigateToProfileFragment : MyEvent()
    }
}