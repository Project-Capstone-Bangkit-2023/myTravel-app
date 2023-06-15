package com.capstoneproject.mytravel.retrofit

import com.google.gson.annotations.SerializedName

data class RecommendResponse(

	@field:SerializedName("data")
	val data: List<RecommendItem>,

	@field:SerializedName("status")
	val status: String
)

data class RecommendItem(

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

	@field:SerializedName("picture")
	val picture: String,

	@field:SerializedName("countRating")
	val countRating: Int,

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
