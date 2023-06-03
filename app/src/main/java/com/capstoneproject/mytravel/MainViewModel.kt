package com.capstoneproject.mytravel

import androidx.lifecycle.*
import com.capstoneproject.mytravel.model.UserModel
import com.capstoneproject.mytravel.model.UserPreference
import com.capstoneproject.mytravel.retrofit.ApiConfig
import com.capstoneproject.mytravel.retrofit.LoginResponse
import com.capstoneproject.mytravel.retrofit.ProfileResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel(private val pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun login(user: UserModel) {
        viewModelScope.launch {
            pref.login(user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}