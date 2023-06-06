package com.capstoneproject.mytravel.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstoneproject.mytravel.model.UserModel
import com.capstoneproject.mytravel.model.UserPreference
import com.capstoneproject.mytravel.retrofit.ApiConfig
import com.capstoneproject.mytravel.retrofit.DataItem
import com.capstoneproject.mytravel.retrofit.SearchResponse
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


    companion object{
        private const val TAG = "HomeViewModel"
    }

    fun findPlaces(token: String, txtQuery : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchPlace(token, txtQuery)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listPlace.value = response.body()?.data
                    println("SUCCESS")
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    println(response.message())
                }
            }
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                failureToast.value = true
            }
        })
    }
}