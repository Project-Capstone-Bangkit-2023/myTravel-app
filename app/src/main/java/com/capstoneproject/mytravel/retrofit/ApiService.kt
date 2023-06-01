package com.capstoneproject.mytravel.retrofit

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String
    ):Call<LoginResponse>

    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("location") location: String,
        @Field("cat_pref") catPref: String
    ): Call<RegisterResponse>

    @GET("search/users")
    fun getUser(
        @Query("q") q: String
    ): Call<GithubResponse>

    @GET("profile/{email}")
    fun getProfile(
        @Header("Authorization") token : String,
        @Path("email") email : String
    ): Call<ProfileResponse>

}
