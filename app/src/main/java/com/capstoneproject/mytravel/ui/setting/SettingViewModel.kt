package com.capstoneproject.mytravel.ui.setting

import androidx.lifecycle.*
import com.capstoneproject.mytravel.model.UserModel
import com.capstoneproject.mytravel.model.UserPreference
import com.capstoneproject.mytravel.retrofit.ApiConfig
import com.capstoneproject.mytravel.retrofit.ProfileResponse
import com.capstoneproject.mytravel.retrofit.UserData
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingViewModel(private val pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _dataProfile = MutableLiveData<UserData>()
    val dataProfile: LiveData<UserData> = _dataProfile
    fun getProfile(token: String, email: String){
        _isLoading.value = true
        val service = ApiConfig.getApiService().getProfile(token, email)
        service.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _dataProfile.value = response.body()?.user
                } else {
                    _isLoading.value = false
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
    fun login(user: UserModel) {
        viewModelScope.launch {
            pref.login(user)
        }
    }
}