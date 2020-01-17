package com.yllxh.currentweather.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class City(
    @SerializedName("coord")    val latLng: LatLng = LatLng(),
    @SerializedName("country")  val country: String = "",
    @SerializedName("name")     val name: String = ""
) : Parcelable