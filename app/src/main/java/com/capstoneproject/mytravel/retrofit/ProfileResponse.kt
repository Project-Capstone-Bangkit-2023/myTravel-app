package com.capstoneproject.mytravel.retrofit

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("status")
	val status: String
)

data class User(

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
	val age: Any,

	@field:SerializedName("cat_pref")
	val catPref: Any
)
