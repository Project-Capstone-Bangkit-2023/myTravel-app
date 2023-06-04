package com.capstoneproject.mytravel.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstoneproject.mytravel.model.UserModel
import com.capstoneproject.mytravel.model.UserPreference
import com.capstoneproject.mytravel.retrofit.ApiConfig
import com.capstoneproject.mytravel.retrofit.CategoryItem
import com.capstoneproject.mytravel.retrofit.CategoryResponse
import com.capstoneproject.mytravel.retrofit.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirstSetupViewModel(private val pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRegisterSuccess = MutableLiveData<Boolean>()
    val isRegisterSuccess: LiveData<Boolean> = _isRegisterSuccess

    private val _listCategory = MutableLiveData<List<CategoryItem>>()
    val listCategory: LiveData<List<CategoryItem>> = _listCategory

    fun register(photoUrl: String, name: String, email: String, location: String, catPref: String, token: String){
        _isLoading.value = true
        val service = ApiConfig.getApiService().register(name, email, location, 22, catPref)
        service.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _isRegisterSuccess.value = true
                    println(response.body()?.message.toString())

                    login(UserModel(0,photoUrl,name,email,location,0,catPref,true,token))

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


    fun login(user: UserModel) {
        viewModelScope.launch {
            pref.login(user)
        }
    }
}