package com.yllxh.currentweather.data.json_models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    @SerializedName("id")           val id: Int = 0,
    @SerializedName("main")         val type: String = "",
    @SerializedName("icon")         val icon: String = "",
    @SerializedName("description")  val description: String = ""
) : Parcelable