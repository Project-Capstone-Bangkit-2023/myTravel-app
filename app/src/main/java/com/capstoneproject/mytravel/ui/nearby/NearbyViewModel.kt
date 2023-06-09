package com.capstoneproject.mytravel.ui.nearby

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstoneproject.mytravel.model.UserModel
import com.capstoneproject.mytravel.model.UserPreference
import com.capstoneproject.mytravel.retrofit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NearbyViewModel(private val pref: UserPreference) : ViewModel()  {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    private val _listPlace = MutableLiveData<List<DataItem>>()
    val listPlace: LiveData<List<DataItem>> = _listPlace

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var failureToast = MutableLiveData<Boolean?>()


    companion object{
        private const val TAG = "Nearby View Model"
    }

    fun findPlaces(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllPlace(token)
        client.enqueue(object : Callback<PlaceResponse> {
            override fun onResponse(
                call: Call<PlaceResponse>,
                response: Response<PlaceResponse>
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
            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                failureToast.value = true
            }
        })
    }


}