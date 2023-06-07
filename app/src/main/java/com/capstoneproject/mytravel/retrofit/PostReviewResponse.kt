package com.capstoneproject.mytravel.retrofit

import com.google.gson.annotations.SerializedName

data class PostReviewResponse(

	@field:SerializedName("data")
	val data: ReviewData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class ReviewData(

	@field:SerializedName("updated_at")
	val updatedAt: Any,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("review")
	val review: String,

	@field:SerializedName("tourism_id")
	val tourismId: Int,

	@field:SerializedName("rating")
	val rating: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int
)
