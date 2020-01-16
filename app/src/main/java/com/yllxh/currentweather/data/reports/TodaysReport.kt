package com.yllxh.currentweather.data.reports


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.yllxh.currentweather.data.*
import com.yllxh.currentweather.utils.fromSecondsToDayIndex
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TodaysReport(
    @SerializedName("dt")       override val timeInSeconds: Int = 0,
    @SerializedName("name")     val cityName: String = "",
    @SerializedName("sys")      private val countryInfo: CountryInfo = CountryInfo(),
    @SerializedName("main")     private val mainInfo: MainInfo = MainInfo(),
    @SerializedName("wind")     private val wind: Wind = Wind(),
    @SerializedName("clouds")   private val clouds: Clouds = Clouds(),
    @SerializedName("weather")  private val weather: List<Weather> = listOf()
) : Report, Parcelable {
    override val temp: Double get() = mainInfo.temp
    override val tempMin: Double get() = mainInfo.tempMin
    override val tempMax: Double get() = mainInfo.tempMax
    override val tempFeels: Double get() = mainInfo.feelsLike
    override val pressure: Int get() = mainInfo.pressure
    override val humidity: Int get() = mainInfo.humidity
    override val windSpeed: Double get() = wind.speed
    override val windDegree: Int get() = wind.deg
    override val cloudiness: Int get() = clouds.cloudiness
    override val icon get() = weather[0].icon
    override val weatherType: String get() = weather[0].type
    override val description: String get() = weather[0].description
    override val day: Int get() = fromSecondsToDayIndex(timeInSeconds)
    override val weatherId: Int get() = weather[0].id
    val country: String get() = countryInfo.country
}



