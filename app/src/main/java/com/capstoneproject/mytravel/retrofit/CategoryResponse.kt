package com.capstoneproject.mytravel.retrofit

import com.google.gson.annotations.SerializedName

data class CategoryResponse(

	@field:SerializedName("category")
	val category: List<CategoryItem>,

	@field:SerializedName("status")
	val status: String
)

data class CategoryItem(

	@field:SerializedName("updated_at")
	val updatedAt: Any,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int
)
