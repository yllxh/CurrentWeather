package com.yllxh.currentweather.utils

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class SearchState : Parcelable {
    UNDEFINED,
    SEARCHING,
    SUCCEEDED,
    FAILED,
    MISSING_LOCATION_PERMISSION
}