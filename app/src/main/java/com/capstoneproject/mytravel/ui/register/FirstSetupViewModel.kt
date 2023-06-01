package com.capstoneproject.mytravel.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.mytravel.retrofit.ApiConfig
import com.capstoneproject.mytravel.retrofit.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirstSetupViewModel  : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRegisterSuccess = MutableLiveData<Boolean>()
    val isRegisterSuccess: LiveData<Boolean> = _isRegisterSuccess

    fun register(name: String, email: String, location: String, catPref: String){
        _isLoading.value = true
        val service = ApiConfig.getApiService().register(name, email, location, catPref)
        service.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _isRegisterSuccess.value = true
                    println(response.body()?.message.toString())

                } else {
                    _isLoading.value = false
                    _isRegisterSuccess.value = false
                    println(response.body()?.message.toString())
                    Log.e("REGISTER", "onFailure: ${response.message()}")
                    Log.e("REGISTER", "onFailure: ${response.errorBody()}")
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _isRegisterSuccess.value = false
            }
        })
    }
}