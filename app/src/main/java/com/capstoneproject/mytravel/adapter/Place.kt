package com.capstoneproject.mytravel.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Place(
    val id: Int?,
    val name: String?,
    val category: String?,
    val photo: String?,
    val city: String?,
    val rating: Double?,
    val price: Int?,
    val desc: String?,
    val lat: Double?,
    val lon: Double?
) : Parcelable