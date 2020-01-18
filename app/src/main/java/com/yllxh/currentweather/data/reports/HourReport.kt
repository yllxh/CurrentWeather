package com.yllxh.currentweather.data.reports


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.yllxh.currentweather.data.json_models.Clouds
import com.yllxh.currentweather.data.json_models.MainInfo
import com.yllxh.currentweather.data.json_models.Weather
import com.yllxh.currentweather.data.json_models.Wind
import com.yllxh.currentweather.utils.fromSecondsToDayIndex
import kotlinx.android.parcel.Parcelize

@Parcelize
class HourReport(
    @SerializedName("clouds")   private val clouds: Clouds = Clouds(),
    @SerializedName("dt")       override val timeInSeconds: Int = 0,
    @SerializedName("main")     private val mainInfo: MainInfo = MainInfo(),
    @SerializedName("weather")  private val weather: List<Weather> = listOf(),
    @SerializedName("wind")     private val wind: Wind = Wind()
) : Report, Parcelable {
    override val temp: Double get() = mainInfo.temp
    override val tempMin: Double get()  = mainInfo.tempMin
    override val tempMax: Double get() = mainInfo.tempMax
    override val tempFeels: Double get() = mainInfo.feelsLike
    override val pressure: Int get() = mainInfo.pressure
    override val humidity: Int get() = mainInfo.humidity
    override val windSpeed: Double get()  = wind.speed
    override val windDegree: Int get() = wind.deg
    override val cloudiness: Int get() = clouds.cloudiness
    override val icon: String get() = weather[0].icon
    override val weatherType: String get() = weather[0].type
    override val description: String get() = weather[0].description
    override val day: Int get() = fromSecondsToDayIndex(timeInSeconds)
    override val weatherId: Int get() = weather[0].id
}