package com.capstoneproject.mytravel.ui.gem

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.mytravel.retrofit.ApiConfig
import com.capstoneproject.mytravel.retrofit.GithubResponse
import com.capstoneproject.mytravel.retrofit.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GemViewModel : ViewModel() {


    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var failureToast = MutableLiveData<Boolean?>()


    companion object{
        private const val TAG = "RecommendViewModel"
    }

    init{
        findUser("adam")
    }
    fun findUser(txtQuery : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(txtQuery)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                failureToast.value = true
            }
        })
    }
}