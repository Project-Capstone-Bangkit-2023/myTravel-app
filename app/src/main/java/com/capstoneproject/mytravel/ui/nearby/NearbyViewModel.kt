package com.capstoneproject.mytravel.ui.nearby

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstoneproject.mytravel.BuildConfig
import com.capstoneproject.mytravel.adapter.Nearby
import com.capstoneproject.mytravel.model.UserModel
import com.capstoneproject.mytravel.model.UserPreference
import com.capstoneproject.mytravel.retrofit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NearbyViewModel(private val pref: UserPreference) : ViewModel()  {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
    private val _listPlace = MutableLiveData<List<Nearby>>()
    val listPlace: LiveData<List<Nearby>> = _listPlace

    private val _place = MutableLiveData<List<DataItem>>()
    val place: LiveData<List<DataItem>> = _place

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var failureToast = MutableLiveData<Boolean?>()

    private val _count = MutableLiveData<Int>(0)

    companion object{
        private const val TAG = "Nearby View Model"
        private const val API_KEY = BuildConfig.API_KEY
    }

    fun findPlaces(lat: Double, lon: Double, token: String, fieldMask: String) {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://routes.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllPlace(token)
        client.enqueue(object : Callback<PlaceResponse> {
            override fun onResponse(
                call: Call<PlaceResponse>,
                response: Response<PlaceResponse>
            ) {
                if (response.isSuccessful) {
                    val listPlace = listPlace.value?.toMutableList() ?: mutableListOf()
                    _place.value = response.body()?.data
                    if(_count.value!! != 10) {
                        for(i in _place.value!!){
                            if(_count.value!!.toInt() == 10){
                                break
                            }
                            val latDestination = i.latitude.toDouble()
                            val lonDestination = i.longitude.toDouble()

                            val request = RouteRequest(
                                Origin(Location(LatLng(lat, lon))),
                                Destination(Location(LatLng(latDestination, lonDestination)))
                            )
                            val clientRoute = apiService.computeRoute(API_KEY, fieldMask , request)
                            clientRoute.enqueue(object : Callback<RouteResponse> {
                                override fun onResponse(
                                    call: Call<RouteResponse>,
                                    response: Response<RouteResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        val distanceMeter = response.body()?.routes?.get(0)?.distanceMeters
                                        if (distanceMeter != null) {
                                            if(distanceMeter < 30000.0){
                                                if(_count.value!!.toInt() < 10) {
                                                    _count.value = _count.value!! + 1
                                                    val doubleDistanceMeter = distanceMeter.toDouble()
                                                    val strRating = i.rating.toString()
                                                    val distanceKm: Double =
                                                        (doubleDistanceMeter / 1000)
                                                    val place = Nearby(
                                                        i.id,
                                                        i.name,
                                                        i.category,
                                                        i.picture,
                                                        i.city,
                                                        strRating.toDouble(),
                                                        i.price,
                                                        i.description,
                                                        latDestination,
                                                        lonDestination,
                                                        distanceKm
                                                    )
                                                    listPlace.add(place)
                                                }else{
                                                    _isLoading.value = false
                                                    _listPlace.value = listPlace
                                                }
                                            }
                                        }
                                    } else {
                                        Log.e("GET ROUTE", "onFailure: ${response.message()}")
                                        println(response.message())
                                    }
                                }
                                override fun onFailure(call: Call<RouteResponse>, t: Throwable) {
                                    Log.e("ERROR", "onFailure: ${t.message.toString()}")
                                }
                            })

                        }
                    }else{
                        _isLoading.value = false
                    }
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