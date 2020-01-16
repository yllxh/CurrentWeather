package com.yllxh.currentweather.data

import com.yllxh.currentweather.data.remote.WeatherApi
import com.yllxh.currentweather.data.reports.TodaysReport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository {
    suspend fun fetchTodaysReportForLocation(
        latLng: LatLng,
        unitType: String,
        lang: String
    ): TodaysReport {
        return withContext(Dispatchers.IO) {
                return@withContext WeatherApi
                    .weatherApiService
                    .getReportFromLocationAsync(latLng.lat, latLng.lng, unitType, lang)
                    .await()
        }
    }


}
