package com.capstoneproject.mytravel.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstoneproject.mytravel.model.UserModel
import com.capstoneproject.mytravel.model.UserPreference
import com.capstoneproject.mytravel.retrofit.ApiConfig
import com.capstoneproject.mytravel.retrofit.DataItem
import com.capstoneproject.mytravel.retrofit.PlaceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val pref: UserPreference) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    private val _listPlace = MutableLiveData<List<DataItem>>()
    val listPlace: LiveData<List<DataItem>> = _listPlace

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var failureToast = MutableLiveData<Boolean?>()

    fun findPlaces(token: String, txtQuery : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchPlace(token, txtQuery)
        client.enqueue(object : Callback<PlaceResponse> {
            override fun onResponse(
                call: Call<PlaceResponse>,
                response: Response<PlaceResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listPlace.value = response.body()?.data
                } else {

                }
            }
            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                _isLoading.value = false
                failureToast.value = true
            }
        })
    }
}