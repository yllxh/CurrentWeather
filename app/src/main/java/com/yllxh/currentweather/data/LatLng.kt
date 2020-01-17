package com.yllxh.currentweather.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

val VALID_LAT_RANGE = -90..90
val VALID_LNG_RANGE = -180..180

@Parcelize
data class LatLng(
    @SerializedName("lat")  val lat: Double = 0.0,
    @SerializedName("lon")  val lng: Double = 0.0
): Parcelable {
    fun isValid(): Boolean {
        return lat.toInt() in VALID_LAT_RANGE
                && lat.toInt() in VALID_LNG_RANGE
    }
}