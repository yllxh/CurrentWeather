package com.yllxh.currentweather.utils
import android.content.Context
import android.location.Location
import com.google.android.gms.location.*

class LocationRetriever(private val context: Context,
                        private val onLocationReceived: (Location) -> Unit) {


    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private var locationReceivedCallBack: LocationCallback? = null

    init {
        requestDeviceLocation()
    }

    private fun requestDeviceLocation() {
        if (locationReceivedCallBack == null) {
            initLocationReceivedCallBack()
        }
        if (fusedLocationProvider == null) {
            fusedLocationProvider =
                LocationServices.getFusedLocationProviderClient(context)
        }
        fusedLocationProvider?.requestLocationUpdates(
            getLocationRequest(),
            locationReceivedCallBack,
            null
        )
    }

    private fun getLocationRequest(): LocationRequest? {
        return LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun initLocationReceivedCallBack() {
        locationReceivedCallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation.let {
                    it?.let {
                        onLocationReceived(it)
                        log(it.toString())
                        fusedLocationProvider?.removeLocationUpdates(locationReceivedCallBack)
                    }
                }
            }
        }
    }
}