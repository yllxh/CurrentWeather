package com.yllxh.currentweather.viewmodels

import android.app.Application
import android.location.Location
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.yllxh.currentweather.AppRepository
import com.yllxh.currentweather.R
import com.yllxh.currentweather.data.reports.TodaysReport
import com.yllxh.currentweather.data.reports.WeekReport
import com.yllxh.currentweather.utils.*
import retrofit2.HttpException

class MainViewModel(app: Application) : AndroidViewModel(app), NetworkAlerter.NetworkStateListener {

    private val isSavedLocationValid get() = isValidLocationSaved(getApplication())
    private val savedLocation get() = getLastSavedLocation(getApplication())
    private val savedUnitType get() = getUnitType(getApplication())
    private val language get() = getSavedUILanguage(getApplication())

    private val repository = AppRepository()

    private val _todaysReport = MutableLiveData<TodaysReport>()
    val todaysReport: LiveData<TodaysReport> get() = _todaysReport

    private val _weekReport = MutableLiveData<WeekReport>()
    val weekReport: LiveData<WeekReport> get() = _weekReport

    private val _unitType = MutableLiveData<String>(savedUnitType)

    val isCelsiusSelected: LiveData<Boolean> = Transformations.map(_unitType) { it == CELSIUS }

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> get() = _isConnected

    private val _isLocationRequested = MutableLiveData<Boolean>()
    val isLocationRequested: LiveData<Boolean> get() = _isLocationRequested

    private val _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState> get() = _searchState

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

            val todaysReportAsync =
                repository.fetchTodaysReportForLocationAsync(savedLocation, savedUnitType, language)

            val weekReportAsync =
                repository.fetchWeekReportForLocationAsync(savedLocation, savedUnitType, language)

            updateReports(todaysReportAsync.await(), weekReportAsync.await())

            _searchState.toNew(SearchState.SUCCEEDED)

        } catch (e: HttpException) {
            _searchState.toNew(SearchState.FAILED)
        }
    }

    private fun updateReports(todaysReport: TodaysReport, weekReport: WeekReport) {
        log(weekReport.toString())
        _todaysReport.to(todaysReport)
        _weekReport.to(weekReport)
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
        _searchState.to(SearchState.MISSING_LOCATION_PERMISSION)
    }

    fun onNotConnectedDialogDismissed() {
        if (isConnected.isFalse()) {
            _isConnected.toSelf()
        }
    }

    fun onUnitTypeClicked(view: View) {
        val newUnitType = when (view.id) {
            R.id.celsius_button -> CELSIUS
            else -> FAHRENHEIT
        }
        val isSaved = _unitType.toNew(newUnitType)

        if (isSaved) {
            setUnitType(getApplication(), newUnitType)
            getTodaysWeatherReport()
        }
    }

    fun onMyLocationClicked(){
        _isLocationRequested.to(true)
    }

    override fun onCleared() {
        super.onCleared()
        networkAlerter.stopListening()
    }
}
