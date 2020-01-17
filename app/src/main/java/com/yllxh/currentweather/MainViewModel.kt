package com.yllxh.currentweather

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.yllxh.currentweather.data.reports.TodaysReport
import com.yllxh.currentweather.utils.*
import retrofit2.HttpException

class MainViewModel(app: Application) : AndroidViewModel(app), NetworkAlerter.NetworkStateListener {

    private val isSavedLocationValid get() = isValidLocationSaved(getApplication())
    private val savedLocation get() = getLastSavedLocation(getApplication())
    private val unitType get() = getUnitType(getApplication())
    private val language get() = getSavedUILanguage(getApplication())

    private val repository = AppRepository()

    private val _todaysReport = MutableLiveData<TodaysReport>()
    val todaysReport get() = _todaysReport

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected get() = _isConnected

    private val _isLocationRequested = MutableLiveData<Boolean>()
    val isLocationRequested get() = _isLocationRequested

    private val _searchState = MutableLiveData<SearchState>()
    val searchState get() = _searchState

    private val networkAlerter = NetworkAlerter(this, getApplication())

    fun getTodaysWeatherReport() {
        _searchState.toNew(SearchState.SEARCHING)
        when {
            isSavedLocationValid -> useLocationToGetReport()
            else -> _isLocationRequested.to(true)
        }
    }



    private fun useLocationToGetReport() = onMainContext {
        try {
            _searchState.toNew(SearchState.SEARCHING)
            _todaysReport.to(
                repository.fetchTodaysReportForLocation(savedLocation, unitType, language)
            )
            _searchState.toNew(SearchState.SUCCEEDED)

        } catch (e: HttpException) {
            _searchState.toNew(SearchState.FAILED)
        }
    }

    override fun onNetworkStateChanged(state: NetworkState) {
        val isConnected: Boolean = when (state) {
            NetworkState.AVAILABLE -> true
            NetworkState.LOST -> false
        }
        _isConnected.toNew(isConnected)
    }

    fun onLocationRetrieved(location: Location) {
        _isLocationRequested.to(false)

        setLastLocation(getApplication(), location.toLatLng())

        if (_isConnected.isTrue()) {
            useLocationToGetReport()
        }
    }

    fun onLocationPermissionApproved() {
        _isLocationRequested.to(true)
    }

    fun onLocationPermissionDenied() {
        if (searchState.isSuccessful())
            return

        _searchState.to(SearchState.MISSING_LOCATION_PERMISSION)

        _isLocationRequested.to(true)
    }

    fun onNotConnectedDialogDismissed() {
        if (isConnected.isFalse()) {
            _isConnected.toSelf()
        }
    }

    override fun onCleared() {
        super.onCleared()
        networkAlerter.stopListening()
    }
}
