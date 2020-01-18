package com.yllxh.currentweather.data.json_models
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Clouds(
    @SerializedName("all") val cloudiness: Int = 0
) : Parcelable