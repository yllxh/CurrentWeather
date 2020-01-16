package com.yllxh.currentweather.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Wind(
    @SerializedName("deg")      val deg: Int = 0,
    @SerializedName("speed")    val speed: Double = 0.0
) : Parcelable