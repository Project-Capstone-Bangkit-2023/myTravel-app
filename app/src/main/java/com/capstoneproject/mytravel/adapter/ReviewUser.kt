package com.capstoneproject.mytravel.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewUser(
    val name: String?,
    val review: String?,
    val rating: Int?,
    val id: Int?,
    val userId: Int?,
) : Parcelable