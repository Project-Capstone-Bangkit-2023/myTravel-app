package com.capstoneproject.mytravel.retrofit

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: DataLogin,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class DataLogin(

	@field:SerializedName("token")
	val token: String
)
