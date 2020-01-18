package com.yllxh.currentweather

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yllxh.currentweather.data.remote.WeatherApi
import com.yllxh.currentweather.data.reports.TodaysReport
import com.yllxh.currentweather.data.reports.WeekReport
import com.yllxh.currentweather.utils.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {

    val savedUnitType get() = getUnitType(context)
    private val savedLocation get() = getLastSavedLocation(context)
    private val language get() = getSavedUILanguage(context)

    private val _todaysReport = MutableLiveData<TodaysReport>()
    val todaysReport: LiveData<TodaysReport> get() = _todaysReport

    private val _weekReport = MutableLiveData<WeekReport>()
    val weekReport : LiveData<WeekReport> get() = _weekReport

    private suspend fun fetchTodaysReportUsingLocationAsync(): Deferred<TodaysReport> {
        return withContext(Dispatchers.IO) {
                return@withContext WeatherApi
                    .weatherApiService
                    .getReportFromLocationAsync(savedLocation.lat, savedLocation.lng, savedUnitType, language)
        }
    }


    private suspend fun fetchWeekReportUsingLocationAsync(): Deferred<WeekReport> {
        return withContext(Dispatchers.IO) {
                return@withContext WeatherApi
                    .weatherApiService
                    .getWeekReportFromLocationAsync(savedLocation.lat, savedLocation.lng, savedUnitType, language)
        }
    }


    private suspend fun fetchTodaysReportUsingCityNameAsync(cityName: String): Deferred<TodaysReport> {
        return withContext(Dispatchers.IO) {
            return@withContext WeatherApi
                .weatherApiService
                .getReportUsingCityNameAsync(cityName, savedUnitType, language)
        }
    }

    private suspend fun fetchWeekReportUsingCityNameAsync(cityName: String): Deferred<WeekReport> {
        return withContext(Dispatchers.IO) {
            return@withContext WeatherApi
                .weatherApiService
                .getWeekReportUsingCityName(cityName, savedUnitType, language)
        }
    }



    suspend fun useLocationToFetchReport() {
        val todaysReportAsync =
            fetchTodaysReportUsingLocationAsync()

        val weekReportAsync =
            fetchWeekReportUsingLocationAsync()

        updateReports(todaysReportAsync.await(), weekReportAsync.await())
    }


    suspend fun useCityNameToFetchReport(cityName: String) {
        log("useCityNameToFetchReport")

        val todaysReportAsync =
            fetchTodaysReportUsingCityNameAsync(cityName)

        val weekReportAsync =
            fetchWeekReportUsingCityNameAsync(cityName)

        updateReports(todaysReportAsync.await(), weekReportAsync.await())

        weekReport.value?.let {
            val latLng = it.city.latLng
            setLastLocation(context, latLng)
        }
    }

    private fun updateReports(todaysReport: TodaysReport, weekReport: WeekReport) {
        _todaysReport.to(todaysReport)
        _weekReport.to(weekReport)
    }

}
