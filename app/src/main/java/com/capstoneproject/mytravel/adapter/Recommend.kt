package com.capstoneproject.mytravel.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recommend(
    val name: String?,
    val photo: String?,
    val url: String?,
    val weather: String?
) : Parcelable