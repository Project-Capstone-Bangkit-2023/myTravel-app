package com.capstoneproject.mytravel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.mytravel.model.UserPreference
import com.capstoneproject.mytravel.ui.home.DetailSearchViewModel
import com.capstoneproject.mytravel.ui.home.HomeViewModel
import com.capstoneproject.mytravel.ui.login.LoginViewModel
import com.capstoneproject.mytravel.ui.register.FirstSetupViewModel
import com.capstoneproject.mytravel.ui.setting.SettingViewModel

class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(FirstSetupViewModel::class.java) -> {
                FirstSetupViewModel(pref) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(pref) as T
            }
            modelClass.isAssignableFrom(DetailSearchViewModel::class.java) -> {
                DetailSearchViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}