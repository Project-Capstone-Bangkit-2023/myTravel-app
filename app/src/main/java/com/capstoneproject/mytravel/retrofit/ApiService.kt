package com.capstoneproject.mytravel.retrofit

import retrofit2.Call
import retrofit2.http.*

data class LatLng(val latitude: Double, val longitude: Double)

data class Location(val latLng: LatLng)

data class Origin(val location: Location)

data class Destination(val location: Location)

data class RouteRequest(val origin: Origin, val destination: Destination)


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
    ): Call<PlaceResponse>
    @GET("tourisms")
    fun getAllPlace(
        @Header("Authorization") token: String
    ): Call<PlaceResponse>

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

    @FormUrlEncoded
    @POST("profile/{userId}/update")
    fun updateProfile(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Field("location") location: String,
        @Field("age") age: Int,
        @Field("cat_pref") catPref: String
    ): Call<UpdateProfileResponse>

    @POST("directions/v2:computeRoutes")
    fun computeRoute(
        @Header("X-Goog-Api-Key") key: String,
        @Header("X-Goog-FieldMask") fieldMask: String,
        @Body request: RouteRequest,
    ): Call<RouteResponse>

    @GET("tourisms/recomendations")
    fun getRecommendation(
        @Header("Authorization") token: String
    ): Call<RecommendResponse>

}
