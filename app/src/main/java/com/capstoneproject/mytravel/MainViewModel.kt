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

    private val _connectionFailed = MutableLiveData<Boolean>()
    val connectionFailed: LiveData<Boolean> = _connectionFailed

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> = _token

    fun loginProcess(email: String){
        _isLoading.value = true
        val service = ApiConfig.getApiService().login(email)
        service.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _token.value = response.body()?.data?.token
                } else {
                    _isLoading.value = false
                    _token.value = null
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _connectionFailed.value = true
                _isLoading.value = false
            }
        })
    }

    fun getProfile(token: String, email: String, photoUrl: String){
        _isLoading.value = true
        val service = ApiConfig.getApiService().getProfile(token, email)
        service.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val body = response.body()
                    val name = body?.user?.name.toString()
                    val emailBody = body?.user?.email.toString()
                    val strId = body?.user?.id.toString()
                    val id = strId.toInt()
                    val location = body?.user?.location.toString()
                    val catPref = body?.user?.catPref.toString()

                    login(UserModel(id,photoUrl,name,emailBody,location,0,catPref,true,token))
                } else {
                    _isLoading.value = false
                    _token.value = null
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                _connectionFailed.value = true
                _isLoading.value = false
            }
        })
    }

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