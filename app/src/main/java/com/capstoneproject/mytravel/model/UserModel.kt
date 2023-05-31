package com.capstoneproject.mytravel.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class  UserModel(
    val userId: Int,
    val photo_url: String,
    val name: String,
    val email: String,
    val location: String,
    val age: Int,
    val isLogin: Boolean,
    val token: String
): Parcelable