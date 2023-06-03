package com.capstoneproject.mytravel.ui.nearby

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

class NearbyViewModel : ViewModel() {

    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var failureToast = MutableLiveData<Boolean?>()


    companion object{
        private const val TAG = "RecommendViewModel"
    }

}