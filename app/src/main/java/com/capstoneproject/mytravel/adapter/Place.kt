package com.capstoneproject.mytravel.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Place(
    val name: String?,
    val category: String?,
    val photo: String?,
    val city: String?,
    val rating: Double?,
    val lat: Double?,
    val lon: Double?
) : Parcelable