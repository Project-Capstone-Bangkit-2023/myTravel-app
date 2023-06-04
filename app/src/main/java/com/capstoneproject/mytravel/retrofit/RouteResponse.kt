package com.capstoneproject.mytravel.retrofit

import com.google.gson.annotations.SerializedName

data class RouteResponse(

	@field:SerializedName("routes")
	val routes: List<RoutesItem>
)

data class Polyline(

	@field:SerializedName("encodedPolyline")
	val encodedPolyline: String
)

data class RoutesItem(

	@field:SerializedName("duration")
	val duration: String,

	@field:SerializedName("distanceMeters")
	val distanceMeters: Int,

	@field:SerializedName("polyline")
	val polyline: Polyline
)
