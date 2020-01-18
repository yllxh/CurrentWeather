package com.yllxh.currentweather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yllxh.currentweather.data.json_models.LatLng
import com.yllxh.currentweather.data.remote.WeatherApi
import com.yllxh.currentweather.data.reports.TodaysReport
import com.yllxh.currentweather.data.reports.WeekReport
import com.yllxh.currentweather.utils.log
import com.yllxh.currentweather.utils.to
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository {


    private val _todaysReport = MutableLiveData<TodaysReport>()
    val todaysReport: LiveData<TodaysReport> get() = _todaysReport

    private val _weekReport = MutableLiveData<WeekReport>()
    val weekReport : LiveData<WeekReport> get() = _weekReport

    private suspend fun fetchTodaysReportUsingLocationAsync(
        latLng: LatLng,
        unitType: String,
        lang: String
    ): Deferred<TodaysReport> {
        return withContext(Dispatchers.IO) {
                return@withContext WeatherApi
                    .weatherApiService
                    .getReportFromLocationAsync(latLng.lat, latLng.lng, unitType, lang)
        }
    }


    private suspend fun fetchWeekReportUsingLocationAsync(
        latLng: LatLng,
        unitType: String,
        lang: String
    ): Deferred<WeekReport> {
        return withContext(Dispatchers.IO) {
                return@withContext WeatherApi
                    .weatherApiService
                    .getWeekReportFromLocationAsync(latLng.lat, latLng.lng, unitType, lang)
        }
    }



    suspend fun useLocationToFetchReport(latLng: LatLng, unitType: String, lang: String) {
        val todaysReportAsync =
            fetchTodaysReportUsingLocationAsync(latLng, unitType, lang)

        val weekReportAsync =
            fetchWeekReportUsingLocationAsync(latLng, unitType, lang)

        updateReports(todaysReportAsync.await(), weekReportAsync.await())
    }


    private fun updateReports(todaysReport: TodaysReport, weekReport: WeekReport) {
        _todaysReport.to(todaysReport)
        _weekReport.to(weekReport)
    }

}
