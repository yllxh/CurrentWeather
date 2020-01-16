package com.yllxh.currentweather

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.yllxh.currentweather.data.Repository
import com.yllxh.currentweather.data.reports.TodaysReport
import com.yllxh.currentweather.utils.*
import retrofit2.HttpException

class MainViewModel(app: Application) : AndroidViewModel(app), NetworkAlerter.NetworkStateListener {

    private val isValidLocationSaved get() = isValidLocationSaved(getApplication())
    private val savedLocation get() = getLastSavedLocation(getApplication())
    private val unitType get() = getUnitType(getApplication())
    private val language get() = getSavedUILanguage(getApplication())

    private var hasLocationPermission: Boolean = false
    private val repository = Repository()

    private val _todaysReport = MutableLiveData<TodaysReport>()
    val todaysReport get() = _todaysReport

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected get() = _isConnected

    private val _searchState = MutableLiveData<SearchState>()
    val searchState get() = _searchState


    override fun onNetworkStateChanged(state: NetworkState) {
        val isConnected: Boolean = when (state) {
            NetworkState.AVAILABLE -> true
            else -> {
                false
            }
        }
        onMainContext { _isConnected.value = isConnected }

        if (!isConnected) {
            return
        } else {
            getTodaysWeatherReport()
        }
    }

    private fun getTodaysWeatherReport() {
            useLocationToGetReport()
    }

    private fun useLocationToGetReport() {
        onMainContext {
            try {
                _searchState.value = SearchState.SEARCHING

                _todaysReport.value =
                    repository.fetchTodaysReportForLocation(savedLocation, unitType, language)

                _searchState.value = SearchState.SUCCEEDED
            } catch (e: HttpException) {
                _searchState.value = SearchState.FAILED
            }

        }
    }

}
