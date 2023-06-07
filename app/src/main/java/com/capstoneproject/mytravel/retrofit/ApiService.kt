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
        @Field("age") age: Int,
        @Field("cat_pref") catPref: String
    ): Call<RegisterResponse>

    @GET("profile/{email}")
    fun getProfile(
        @Header("Authorization") token: String,
        @Path("email") email: String
    ): Call<ProfileResponse>

    @GET("tourisms")
    fun searchPlace(
        @Header("Authorization") token: String,
        @Query("q") q: String
    ): Call<SearchResponse>

    @GET("tourisms/{id}/detail")
    fun getPlaceDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<PlaceDetailResponse>

    @FormUrlEncoded
    @POST("tourisms/{tourism_id}/reviews")
    fun postReviews(
        @Header("Authorization") token: String,
        @Path("tourism_id") tourismId: Int,
        @Field("tourism_id") idTourism: Int,
        @Field("user_id") userId: Int,
        @Field("rating") rating: Int,
        @Field("review") review: String
    ): Call<PostReviewResponse>

    @FormUrlEncoded
    @POST("tourisms/{tourism_id}/reviews/{review_id}")
    fun postUpdateReview(
        @Header("Authorization") token: String,
        @Path("tourism_id") tourismId: Int,
        @Path("review_id") reviewId: Int,
        @Field("rating") rating: Int,
        @Field("review") review: String
    ): Call<UpdateReviewResponse>
}
