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

const val USE_LOCATION = 0
const val USE_CITY_NAME = 1

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val isSavedLocationValid get() = isValidLocationSaved(getApplication())

    private val repository = AppRepository(getApplication())

    val todaysReport: LiveData<TodaysReport> get() = repository.todaysReport
    val weekReport: LiveData<WeekReport> get() = repository.weekReport

    private val _unitType = MutableLiveData<String>(repository.savedUnitType)

    val isCelsiusSelected: LiveData<Boolean> = Transformations.map(_unitType) { it == CELSIUS }

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> get() = _isConnected

    private val _isLocationRequested = MutableLiveData<Boolean>()
    val isLocationRequested: LiveData<Boolean> get() = _isLocationRequested

    private val _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState> get() = _searchState

    private val networkAlerter = NetworkAlerter(getApplication()){ _isConnected.toNew(it) }

    fun getTodaysWeatherReport() {
        _searchState.toNew(SearchState.SEARCHING)
        when {
            isSavedLocationValid -> fetchWeatherData(USE_LOCATION)
            else -> _isLocationRequested.to(true)
        }
    }

    private fun fetchWeatherData(searchType: Int, cityName: String = "") = onMainContext {
        try {
            _searchState.toNew(SearchState.SEARCHING)

            when (searchType) {
                USE_LOCATION -> repository.useLocationToFetchReport()
                USE_CITY_NAME -> repository.useCityNameToFetchReport(cityName)
            }
            _searchState.toNew(SearchState.SUCCEEDED)
        } catch (e: HttpException) {
            _searchState.toNew(SearchState.FAILED)
        }
    }

    fun onLocationRetrieved(location: Location) {
        _isLocationRequested.to(false)

        setLastLocation(getApplication(), location.toLatLng())

        if (_isConnected.isTrue()) {
            fetchWeatherData(USE_LOCATION)
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
