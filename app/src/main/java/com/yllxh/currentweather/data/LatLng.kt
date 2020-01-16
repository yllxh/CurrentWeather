package com.yllxh.currentweather.data

val VALID_LAT_RANGE = -90..90
val VALID_LNG_RANGE = -180..180

data class LatLng(val lat: Double = 0.0, val lng: Double = 0.0){
    fun isValid(): Boolean {
        return lat.toInt() in VALID_LAT_RANGE
                && lat.toInt() in VALID_LNG_RANGE
    }
}