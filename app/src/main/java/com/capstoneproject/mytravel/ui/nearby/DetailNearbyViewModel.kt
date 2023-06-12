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

class DetailNearbyViewModel(private val pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isPostSuccess = MutableLiveData<Boolean>()
    val isPostSuccess: LiveData<Boolean> = _isPostSuccess

    private val _listReviews = MutableLiveData<List<TourismRatingItem>>()
    val listReviews: LiveData<List<TourismRatingItem>> = _listReviews

    private val _reviewUser = MutableLiveData<UserReview>()
    val reviewUser: LiveData<UserReview> = _reviewUser

    private val _review = MutableLiveData<TourismRatingItem>()
    val review: LiveData<TourismRatingItem> = _review



    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getReviews(token: String, id: Int, idUser: Int) {
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
                        println(idReview)
                        if(idUser == idReview){
                            _review.value = i
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

    fun postReview(token: String, tourismId: Int, userId: Int, rating: Int, review: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().postReviews(token, tourismId, tourismId, userId, rating, review)
        client.enqueue(object : Callback<PostReviewResponse> {
            override fun onResponse(
                call: Call<PostReviewResponse>,
                response: Response<PostReviewResponse>
            ) {
                if(response.isSuccessful){
                    _isLoading.value = false
                    _isPostSuccess.value = response.isSuccessful
                    println(response.message())
                }else{
                    _isLoading.value = false
                    _isPostSuccess.value = response.isSuccessful
                    println(response.message())
                }
            }
            override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun postUpdateReview(token: String, tourismId: Int, reviewId: Int, rating: Int, review: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().postUpdateReview(token, tourismId, reviewId, rating, review)
        client.enqueue(object : Callback<UpdateReviewResponse> {
            override fun onResponse(
                call: Call<UpdateReviewResponse>,
                response: Response<UpdateReviewResponse>
            ) {
                if(response.isSuccessful){
                    _isLoading.value = false
                    _isPostSuccess.value = response.isSuccessful
                    println(response.body()?.data)
                    println(response.message())
                }else{
                    _isLoading.value = false
                    _isPostSuccess.value = response.isSuccessful
                    println(response.message())
                }
            }
            override fun onFailure(call: Call<UpdateReviewResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}
