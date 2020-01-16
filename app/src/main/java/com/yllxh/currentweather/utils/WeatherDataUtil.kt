package com.yllxh.currentweather.utils

import com.yllxh.currentweather.data.reports.DayReport
import com.yllxh.currentweather.data.reports.HourReport
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Function used to calculate the average temperature form
 * the list of HourReports that is passed to it.
 *
 * @param   hourlyReports List of hourReports to be processed.
 *
 * @return  Average temperature as a Double.
 */
fun averageTemp(hourlyReports: List<HourReport>): Double {
    return if (hourlyReports.isEmpty()) 0.0
    else {
        val sum = hourlyReports.map { it.temp }
            .reduce { acc, next -> acc + next }
        (sum / hourlyReports.size)
    }
}

/**
 * Function used to find the minimum temperature form
 * the list of HourReports that is passed to it.
 *
 * @param   hourlyReports List of hourReports to processed.
 *
 * @return  Minimum temperature as a Double
 */
fun minTemp(hourlyReports: List<HourReport>): Double {
    return if (hourlyReports.isEmpty()) 0.0
    else {
        hourlyReports.map { it.tempMin }.min() ?: 0.0
    }
}

/**
 * Function used to find the maximum temperature form
 * the list of HourReports that is passed to it.
 *
 * @param   hourlyReports List of hourReports to processed.
 *
 * @return  Maximum temperature as a Double
 */
fun maxTemp(hourlyReports: List<HourReport>): Double {
    return if (hourlyReports.isEmpty()) 0.0
    else {
        hourlyReports.map { it.tempMax }.max() ?: 0.0
    }
}

/**
 * Function used to find the average feels like temperature
 * form the list of HourReports that is passed to it.
 *
 * @param   hourlyReports List of hourReports to processed.
 *
 * @return  Average feels like temperature as a Double.
 */
fun averageFeelsLikeTemp(hourlyReports: List<HourReport>): Double {
    return if (hourlyReports.isEmpty()) 0.0
    else {
        val sum = hourlyReports.map { it.tempFeels }
            .reduce { acc, next -> acc + next }
        sum / hourlyReports.size
    }
}

/**
 * Function used to find the average pressure temperature
 * form the list of HourReports that is passed to it.
 *
 * @param   hourlyReports List of hourReports to processed.
 *
 * @return  Average pressure as an Integer.
 */
fun averagePressure(hourlyReports: List<HourReport>): Int {
    return if (hourlyReports.isEmpty()) 0
    else {
        hourlyReports.map { it.pressure }
            .reduce { sum, next -> sum + next } / hourlyReports.size
    }
}

/**
 * Function used to find the average humidity temperature
 * form the list of HourReports that is passed to it.
 *
 * @param   hourlyReports List of hourReports to processed.
 *
 * @return  Average humidity as an Integer.
 */
fun averageHumidity(hourlyReports: List<HourReport>): Int {
    return if (hourlyReports.isEmpty()) 0
    else {
        val sum = hourlyReports.map { it.humidity }
            .reduce { sum, next -> sum + next }
        sum / hourlyReports.size
    }
}

/**
 * Function used to find the average wind speed temperature
 * form the list of HourReports that is passed to it.
 *
 * @param   hourlyReports List of hourReports to processed.
 *
 * @return  Average wind speed as an Double.
 */
fun averageWindSpeed(hourlyReports: List<HourReport>): Double {
    return if (hourlyReports.isEmpty()) 0.0
    else {
        val sum = hourlyReports.map { it.windSpeed }
            .reduce { acc, next -> acc + next }
        sum / hourlyReports.size
    }
}

/**
 * Function used to find the average wind degree temperature
 * form the list of HourReports that is passed to it.
 *
 * @param   hourlyReports List of hourReports to processed.
 *
 * @return  Average wind degree as an Integer.
 */
fun averageWindDegree(hourlyReports: List<HourReport>): Int {

    return if (hourlyReports.isEmpty()) 0
    else {
        val sum = hourlyReports.map { it.windDegree }
            .reduce { acc, next -> acc + next }

        sum / hourlyReports.size
    }
}

/**
 * Function used to find the average cloudiness temperature
 * form the list of HourReports that is passed to it.
 *
 * @param   hourlyReports List of hourReports to processed.
 *
 * @return  Average cloudiness degree as an Integer.
 */
fun averageCloudiness(hourlyReports: List<HourReport>): Int {

    return if (hourlyReports.isEmpty()) 0
    else {
        hourlyReports.map { it.cloudiness }
            .reduce { sum, next -> sum + next } / hourlyReports.size
    }
}

/**
 *  Function used to find the most repeated weather type
 *  from the passed hourlyReports list.
 *  If there are no hourlyReports than it returns an empty strings.
 *
 *  @return  The most repeated weather type as a string.
 */
fun mostRepeatedWeatherType(hourlyReports: List<HourReport>): String {
    return if (hourlyReports.isEmpty()) {
        ""
    } else {
        val sortedPairs = getWeatherTypeList(hourlyReports)
            .sortedBy { it.second }
        getLastItemString(sortedPairs)
    }
}

/**
 * Generates a list of pairs of the weatherType and the number of time
 * it occurs in the list of hourlyReport list.
 *
 * @param   hourlyReports HourlyReports from which the types are extracted.
 *
 * @return  List of pairs of the most repeated weatherType and its count.
 */
fun getWeatherTypeList(hourlyReports: List<HourReport>): List<Pair<String, Int>> {
    return if (hourlyReports.isEmpty()) {
        listOf()
    } else {
        val hashMap = hashMapOf<String, Int>()
        hourlyReports.forEach { hashMap[it.weatherType] = 0 }

        hashMap.keys.map { type ->
            Pair(type, hourlyReports.count { h -> h.weatherType == type })
        }
    }
}

/**
 *  Function used to find the most repeated weather description
 *  from the passed hourlyReports list.
 *  If there are no hourlyReports than it returns an empty strings.
 *
 *  @param   hourlyReports HourlyReports from which the descriptions are extracted.
 *
 *  @return  The most repeated weather description as a string.
 */
fun mostRepeatedWeatherDescription(hourlyReports: List<HourReport>)
        : String {
    return if (hourlyReports.isEmpty()) {
        ""
    } else {
        val sortedPairs = getWeatherDescriptionList(hourlyReports)
            .sortedBy { it.second }
        getLastItemString(sortedPairs)
    }
}

/**
 * Returns list of pairs of the descriptions in the hourlyReports.
 * Generates a list of pairs of the descriptions in the hourlyReports
 * and the number of time it occurs in the list of hourlyReports.
 *
 * @param   hourlyReports HourlyReports from which the descriptions are extracted.
 * @return  The most repeated weather description as a string.
 */
private fun getWeatherDescriptionList(hourlyReports: List<HourReport>): List<Pair<String, Int>> {
    val hashMap = hashMapOf<String, Int>()
    hourlyReports.forEach { hashMap[it.description] = 0 }

    return hashMap.keys.map { type ->
        Pair(type, hourlyReports.count { h -> h.description == type })
    }
}

/**
 * Function used to extract the last string from the list
 * of pairs of string and their count.
 *
 * @param   pairs List of pairs from which to extract the last string
 */
private fun getLastItemString(pairs: List<Pair<String, Int>>): String {
    return if (pairs.isEmpty()) ""
    else {
        pairs.last().first
    }
}


/**
 * Function used to group HourReports by the day that they belong to.
 * If the passed HourReports list is not empty it returns a list
 * of DayReports,otherwise it returns an empty list.
 *
 * @param   hourlyReports  HourReports that need to be grouped to DayReports.
 *
 * @return  If provided HourReports list is not empty returns a list
 *          of DayReports, empty list otherwise.
 *
 */
fun fromHourlyToDayReports(hourlyReports: List<HourReport>): List<DayReport> {
    if (hourlyReports.isEmpty()) {
        return emptyList()
    }

    val hashMap = hashMapOf<Int, List<HourReport>>()

    for (hr in hourlyReports) {
        val day = hr.day
        if (!hashMap.containsKey(day)) {
            hashMap[day] = hourlyReports.filter { it.day == day }
        }
    }

    return hashMap.map { DayReport(it.value) }
        .toMutableList()
        .apply {
            sortBy { it.hourlyReports[0].timeInSeconds }
        }
}

/**
 * Function used to get the index of a day based on the second
 * that are passed.
 *
 * @param   seconds Seconds that need to be converted to a day index.
 *
 * @return  Returns the index of the day that the seconds represent.
 */
fun fromSecondsToDayIndex(seconds: Int): Int {
    val calendar = fromSecondsToCalendar(seconds)

    return when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> 0
        Calendar.TUESDAY -> 1
        Calendar.WEDNESDAY -> 2
        Calendar.THURSDAY -> 3
        Calendar.FRIDAY -> 4
        Calendar.SATURDAY -> 5
        else -> 6
    }
}

private fun fromSecondsToCalendar(seconds: Int): Calendar {
    return DateFormat.getDateTimeInstance().calendar.apply {
        timeInMillis = TimeUnit.SECONDS.toMillis(seconds.toLong())
    }
}