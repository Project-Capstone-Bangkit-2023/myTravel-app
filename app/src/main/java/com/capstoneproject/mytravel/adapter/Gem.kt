package com.capstoneproject.mytravel.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gem(
    val username: String?,
    val photo: String?,
    val url: String?
) : Parcelable