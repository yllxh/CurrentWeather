package com.yllxh.currentweather.data.json_models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryInfo(
    @SerializedName("sunset")   val sunset: Int = 0,
    @SerializedName("sunrise")  val sunrise: Int = 0,
    @SerializedName("country")  val country: String = ""
) : Parcelable