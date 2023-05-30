package com.capstoneproject.mytravel.retrofit

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUser(
        @Query("q") q: String
    ): Call<GithubResponse>

    @GET("users/{username}/followers")
    fun getFollower(
        @Path("username") username : String
    ): Call<List<FollowerResponseItem>>
}
