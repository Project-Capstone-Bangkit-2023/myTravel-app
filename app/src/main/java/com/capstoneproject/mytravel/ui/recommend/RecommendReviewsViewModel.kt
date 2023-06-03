package com.capstoneproject.mytravel.ui.recommend

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstoneproject.mytravel.retrofit.ApiConfig
import com.capstoneproject.mytravel.retrofit.FollowerResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendReviewsViewModel : ViewModel() {
    private val _listUser = MutableLiveData<List<FollowerResponseItem>>()
    val listUser: LiveData<List<FollowerResponseItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "FollowerViewModel"
    }

    fun findUser(txtQuery : String) {
        _isLoading.value = true
    }

}
