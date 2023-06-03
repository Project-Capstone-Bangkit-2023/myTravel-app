package com.capstoneproject.mytravel.ui.setting

import androidx.lifecycle.*
import com.capstoneproject.mytravel.model.UserModel
import com.capstoneproject.mytravel.model.UserPreference
import kotlinx.coroutines.launch

class SettingViewModel(private val pref: UserPreference) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}