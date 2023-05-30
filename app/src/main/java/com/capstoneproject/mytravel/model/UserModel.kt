package com.capstoneproject.mytravel.model

data class  UserModel(
    val name: String,
    val email: String,
    val location: String,
    val age: Int,
    val isLogin: Boolean,
    val token: String
)