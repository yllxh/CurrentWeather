package com.yllxh.currentweather.utils

import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit


fun fromSecondsToDateStamp(seconds: Int): String {
    val calendar = fromSecondsToDate(seconds)

    return DateFormat.getDateInstance().format(calendar.time)
}


private fun fromSecondsToDate(seconds: Int): Date {
    DateFormat.getDateTimeInstance().calendar.apply {
        timeInMillis = TimeUnit.SECONDS.toMillis(seconds.toLong())
        return time
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