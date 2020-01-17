package com.yllxh.currentweather

import com.yllxh.currentweather.data.LatLng
import com.yllxh.currentweather.data.remote.WeatherApi
import com.yllxh.currentweather.data.reports.TodaysReport
import com.yllxh.currentweather.data.reports.WeekReport
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository {
    suspend fun fetchTodaysReportForLocationAsync(
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


    suspend fun fetchWeekReportForLocationAsync(
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


}
