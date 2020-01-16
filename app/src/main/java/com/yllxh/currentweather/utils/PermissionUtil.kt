package com.yllxh.currentweather.utils

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

const val LOCATION_PERMISSION_REQUEST = 123

fun requestLocationPermission(fragment: Fragment, requestCode: Int) {
    fragment.requestPermissions(arrayOf(ACCESS_FINE_LOCATION), requestCode)
}

fun hasLocationPermission(context: Context): Boolean =
    ContextCompat
        .checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED