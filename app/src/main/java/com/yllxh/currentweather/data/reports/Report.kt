package com.yllxh.currentweather.data.reports


interface Report {
    val temp: Double
    val tempMin: Double
    val tempMax: Double
    val tempFeels: Double
    val pressure: Int
    val humidity: Int
    val windSpeed: Double
    val windDegree: Int
    val cloudiness: Int
    val icon: String
    val weatherType: String
    val description: String
    val weatherId: Int
    val timeInSeconds: Int
    val day: Int
}