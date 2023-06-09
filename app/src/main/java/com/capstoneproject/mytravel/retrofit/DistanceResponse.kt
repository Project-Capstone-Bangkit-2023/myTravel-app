package com.capstoneproject.mytravel.retrofit

import com.google.gson.annotations.SerializedName

data class DistanceResponse(

	@field:SerializedName("routes")
	val routes: List<RoutesItems>
)

data class RoutesItems(

	@field:SerializedName("duration")
	val duration: String,

	@field:SerializedName("distanceMeters")
	val distanceMeters: Int,

	@field:SerializedName("polyline")
	val polyline: PolylineArea
)

data class PolylineArea(

	@field:SerializedName("encodedPolyline")
	val encodedPolyline: String
)
