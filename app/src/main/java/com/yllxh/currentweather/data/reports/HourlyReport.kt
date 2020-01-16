package com.yllxh.currentweather.data.reports

import com.yllxh.currentweather.utils.*

abstract class HourlyReport(hourReports: List<HourReport>):
    Report {
    override val temp: Double = averageTemp(hourReports)
    override val tempMin: Double = minTemp(hourReports)
    override val tempMax: Double = maxTemp(hourReports)
    override val tempFeels: Double = averageFeelsLikeTemp(hourReports)
    override val pressure: Int = averagePressure(hourReports)
    override val humidity: Int = averageHumidity(hourReports)
    override val windSpeed: Double = averageWindSpeed(hourReports)
    override val windDegree: Int = averageWindDegree(hourReports)
    override val cloudiness: Int = averageCloudiness(hourReports)
    override val icon: String = "N/D"
    override val weatherType: String = mostRepeatedWeatherType(hourReports)
    override val description: String = mostRepeatedWeatherDescription(hourReports)
    final override val timeInSeconds: Int = getTimeFromReport(hourReports)
    override val day: Int = fromSecondsToDayIndex(timeInSeconds)
    override val weatherId: Int = getWeatherId(hourReports)

    private fun getTimeFromReport(hourReports: List<HourReport>): Int {
        return if (hourReports.isNotEmpty()) hourReports[0].timeInSeconds else 0
    }
    private fun getWeatherId(hourReports: List<HourReport>): Int {
        return if (hourReports.isNotEmpty()) hourReports[0].weatherId else -1
    }
}