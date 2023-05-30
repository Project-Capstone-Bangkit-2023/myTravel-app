package com.capstoneproject.mytravel.retrofit

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email:String
    ):Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name:String,
        @Field("email") email:String
    ): Call<RegisterResponse>

    @GET("search/users")
    fun getUser(
        @Query("q") q: String
    ): Call<GithubResponse>

    @GET("users/{username}/followers")
    fun getFollower(
        @Path("username") username : String
    ): Call<List<FollowerResponseItem>>

}
