package com.capstoneproject.mytravel.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstoneproject.mytravel.model.UserModel
import com.capstoneproject.mytravel.model.UserPreference
import com.capstoneproject.mytravel.retrofit.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirstSetupViewModel(private val pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRegisterSuccess = MutableLiveData<Boolean>()
    val isRegisterSuccess: LiveData<Boolean> = _isRegisterSuccess

    fun register(photoUrl: String, name: String, email: String, location: String, age: Int, catPref: String){
        _isLoading.value = true
        val service = ApiConfig.getApiService().register(name, email, location, age, catPref)
        service.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _isRegisterSuccess.value = true
                    loginProcess(email, photoUrl, age)

                } else {
                    _isLoading.value = false
                    _isRegisterSuccess.value = false
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _isRegisterSuccess.value = false
            }
        })
    }


    fun loginProcess(email: String, photoUrl: String, age: Int){
        _isLoading.value = true
        val service = ApiConfig.getApiService().login(email)
        service.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val tokenBearer = "Bearer " + response.body()?.data?.token.toString()
                    getProfile(tokenBearer, email, photoUrl, age)
                } else {
                    _isLoading.value = false
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun getProfile(token: String, email: String, photoUrl: String, age: Int){
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

                    login(UserModel(id,photoUrl,name,emailBody,location,age,catPref,true,token))
                } else {
                    _isLoading.value = false
                    println(token)
                    println(response.message())
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun login(user: UserModel) {
        viewModelScope.launch {
            pref.login(user)
        }
    }
}