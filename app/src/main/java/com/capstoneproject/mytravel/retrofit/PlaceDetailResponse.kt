package com.capstoneproject.mytravel.retrofit

import com.google.gson.annotations.SerializedName

data class PlaceDetailResponse(

	@field:SerializedName("tourism")
	val tourism: Tourism,

	@field:SerializedName("status")
	val status: String
)

data class Tourism(

	@field:SerializedName("city")
	val city: String,

	@field:SerializedName("latitude")
	val latitude: String,

	@field:SerializedName("rating")
	val rating: Any,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("tourism_rating")
	val tourismRating: List<TourismRatingItem>,

	@field:SerializedName("picture")
	val picture: String,

	@field:SerializedName("updated_at")
	val updatedAt: Any,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("longitude")
	val longitude: String
)

data class UserReview(

	@field:SerializedName("password")
	val password: Any,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("updated_at")
	val updatedAt: Any,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("age")
	val age: Int,

	@field:SerializedName("cat_pref")
	val catPref: Any
)

data class TourismRatingItem(

	@field:SerializedName("updated_at")
	val updatedAt: Any,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("review")
	val review: Any,

	@field:SerializedName("tourism_id")
	val tourismId: Int,

	@field:SerializedName("rating")
	val rating: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("user")
	val user: UserReview
)
