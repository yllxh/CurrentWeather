package com.yllxh.currentweather.data.remote


import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.yllxh.currentweather.data.reports.TodaysReport
import com.yllxh.currentweather.data.reports.WeekReport
import com.yllxh.currentweather.utils.DEFAULT_LANGUAGE
import com.yllxh.currentweather.utils.DEFAULT_UNIT_TYPE
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.openweathermap.org/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface WeatherApiService {

    @GET("data/2.5/weather")
    fun getReportUsingCityNameAsync(@Query("q") cityName: String,
                                    @Query("units") unitType: String? = DEFAULT_UNIT_TYPE,
                                    @Query("lang") lang: String? = DEFAULT_LANGUAGE,
                                    @Query("appid") api:String = WEATHER_API_KEY
    ) : Deferred<TodaysReport>

    @GET("data/2.5/weather")
    fun getReportFromLocationAsync(@Query("lat") latitude: Double,
                                   @Query("lon") longitude: Double,
                                   @Query("units") unitType: String? = DEFAULT_UNIT_TYPE,
                                   @Query("lang") lang: String? = DEFAULT_LANGUAGE,
                                   @Query("appid") api:String = WEATHER_API_KEY
    ): Deferred<TodaysReport>

    @GET("data/2.5/forecast")
    fun getWeekReportUsingCityNameAsync(@Query("q") cityName: String,
                                        @Query("units") unitType: String? = DEFAULT_UNIT_TYPE,
                                        @Query("lang") lang: String? = DEFAULT_LANGUAGE,
                                        @Query("appid") api:String = WEATHER_API_KEY
    ): Deferred<WeekReport>

    @GET("data/2.5/forecast")
    fun getWeekReportFromLocationAsync(@Query("lat") latitude: Double,
                                       @Query("lon") longitude: Double,
                                       @Query("units") unitType: String? = DEFAULT_UNIT_TYPE,
                                       @Query("lang") lang: String? = DEFAULT_LANGUAGE,
                                       @Query("appid") api:String = WEATHER_API_KEY
    ): Deferred<WeekReport>

}

object WeatherApi {
    val weatherApiService : WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}