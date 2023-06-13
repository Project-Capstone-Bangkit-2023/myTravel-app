package com.capstoneproject.mytravel.ui.setting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstoneproject.mytravel.model.UserModel
import com.capstoneproject.mytravel.model.UserPreference
import com.capstoneproject.mytravel.retrofit.ApiConfig
import com.capstoneproject.mytravel.retrofit.RegisterResponse
import com.capstoneproject.mytravel.retrofit.UpdateProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileViewModel(private val pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isUpdateSuccess = MutableLiveData<Boolean>()
    val isUpdateSuccess: LiveData<Boolean> = _isUpdateSuccess

    fun updateProfile(token: String, userId: Int, location: String, age: Int, catPref: String){
        _isLoading.value = true
        val service = ApiConfig.getApiService().updateProfile(token, userId, location, age, catPref)
        service.enqueue(object : Callback<UpdateProfileResponse> {
            override fun onResponse(
                call: Call<UpdateProfileResponse>,
                response: Response<UpdateProfileResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _isUpdateSuccess.value = true

                } else {
                    Log.e("EDIT PROFILE", "onFailure: ${response.message()}")
                    println(response)
                    _isLoading.value = false
                    _isUpdateSuccess.value = false
                }
            }
            override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                _isLoading.value = false
                _isUpdateSuccess.value = false
            }
        })
    }
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}