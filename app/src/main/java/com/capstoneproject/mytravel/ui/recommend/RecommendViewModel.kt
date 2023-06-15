package com.capstoneproject.mytravel.ui.recommend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstoneproject.mytravel.model.UserModel
import com.capstoneproject.mytravel.model.UserPreference
import com.capstoneproject.mytravel.retrofit.ApiConfig
import com.capstoneproject.mytravel.retrofit.DataItem
import com.capstoneproject.mytravel.retrofit.RecommendItem
import com.capstoneproject.mytravel.retrofit.RecommendResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    private val _listPlace = MutableLiveData<List<RecommendItem>>()
    val listPlace: LiveData<List<RecommendItem>> = _listPlace

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var failureToast = MutableLiveData<Boolean?>()

    fun findPlaces(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getRecommendation(token)
        client.enqueue(object : Callback<RecommendResponse> {
            override fun onResponse(
                call: Call<RecommendResponse>,
                response: Response<RecommendResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listPlace.value = response.body()?.data
                } else {
                }
            }
            override fun onFailure(call: Call<RecommendResponse>, t: Throwable) {
                _isLoading.value = false
                failureToast.value = true
            }
        })
    }
}


