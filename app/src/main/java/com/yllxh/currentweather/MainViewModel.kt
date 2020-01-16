package com.yllxh.currentweather

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.yllxh.currentweather.data.Repository
import com.yllxh.currentweather.data.reports.TodaysReport
import com.yllxh.currentweather.utils.*
import retrofit2.HttpException

class MainViewModel(app: Application) : AndroidViewModel(app), NetworkAlerter.NetworkStateListener {

    private val isSavedLocationValid get() = isValidLocationSaved(getApplication())
    private val savedLocation get() = getLastSavedLocation(getApplication())
    private val unitType get() = getUnitType(getApplication())
    private val language get() = getSavedUILanguage(getApplication())

    private val repository = Repository()

    private val _todaysReport = MutableLiveData<TodaysReport>()
    val todaysReport get() = _todaysReport

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected get() = _isConnected

    private val _isLocationRequested = MutableLiveData<Boolean>()
    val isLocationRequested get() = _isLocationRequested

    private val _searchState = MutableLiveData<SearchState>()
    val searchState get() = _searchState


    override fun onNetworkStateChanged(state: NetworkState) = onMainContext {
        val isConnected: Boolean = when (state) {
            NetworkState.AVAILABLE -> true
            else -> {
                false
            }
        }
        _isConnected.value = isConnected

        if (!isConnected) {
            return@onMainContext
        } else {
            getTodaysWeatherReport()
        }
    }

    private fun getTodaysWeatherReport() = when {
        isSavedLocationValid -> useLocationToGetReport()
        else -> _isLocationRequested.value = true
    }

    private fun useLocationToGetReport() = onMainContext {
        try {
            _searchState.searching()

            _todaysReport.value =
                repository.fetchTodaysReportForLocation(savedLocation, unitType, language)

            _searchState.succeeded()
        } catch (e: HttpException) {
            _searchState.failed()
        }

    }

    fun onLocationRetrieved(location: Location) {
        _isLocationRequested.value = false

        setLastLocation(getApplication(), location.toLatLng())

        if (_isConnected.value == true) {
            useLocationToGetReport()
        }
    }

    fun onLocationPermissionDenied() {
        _isLocationRequested.value = false
        _searchState.failed()
    }

    fun onLocationPermissionApproved() {
        _isLocationRequested.value = true
    }

}
