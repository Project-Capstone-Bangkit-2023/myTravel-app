package com.capstoneproject.mytravel.ui.home

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

class DetailSearchViewModel(private val pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listReviews = MutableLiveData<List<TourismRatingItem>>()
    val listReviews: LiveData<List<TourismRatingItem>> = _listReviews

    private val _reviewUser = MutableLiveData<UserReview>()
    val reviewUser: LiveData<UserReview> = _reviewUser

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getReviews(token: String, id: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getPlaceDetail(token, id)
        client.enqueue(object : Callback<PlaceDetailResponse> {
            override fun onResponse(
                call: Call<PlaceDetailResponse>,
                response: Response<PlaceDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listReviews.value = response.body()?.tourism?.tourismRating

                    for(i in _listReviews.value!!){
                        val idReview = i.userId
                        if(id == idReview){
                            _reviewUser.value = i.user
                        }
                    }

                } else {
                    println(response.message())
                }
            }
            override fun onFailure(call: Call<PlaceDetailResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}
