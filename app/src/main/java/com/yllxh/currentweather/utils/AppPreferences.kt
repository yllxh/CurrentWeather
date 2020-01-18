package com.yllxh.currentweather.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.yllxh.currentweather.data.json_models.LatLng
import java.util.*

const val CITY_NOT_SET = "not_set"

const val CELSIUS = "metric"
const val FAHRENHEIT = "imperial"
const val DEFAULT_UNIT_TYPE = CELSIUS

private const val INVALID_COORDINATE = Int.MAX_VALUE.toDouble().toString()

private const val UNIT_TYPE_KEY = "unit_type_key"
private const val CITY_NAME_KEY = "city_name_key"
private const val APP_LANGUAGE_KEY = "app_language_key"
private const val LOCATION_LAT_KEY = "location_lat_key"
private const val LOCATION_LNG_KEY = "location_lon_key"


fun getLastSavedLocation(context: Context): LatLng {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    val lat = getCoordinate(LOCATION_LAT_KEY, preferences)
    val lng = getCoordinate(LOCATION_LNG_KEY, preferences)

    return LatLng(lat, lng)

}

private fun getCoordinate(key: String, preferences: SharedPreferences): Double {
    return if (preferences.contains(key)) {
        preferences.getString(key, INVALID_COORDINATE) ?: INVALID_COORDINATE
    } else {
        setCoordinate(key, INVALID_COORDINATE, preferences)
        INVALID_COORDINATE
    }.toDouble()
}

private fun setCoordinate(key: String, value: String, preferences: SharedPreferences) {
    preferences.apply {
        edit().apply {
            putString(key, value)
            apply()
        }
    }
}

fun setLastLocation(context: Context, latLng: LatLng) {
    PreferenceManager.getDefaultSharedPreferences(context).apply {
        setCoordinate(LOCATION_LAT_KEY, String.format("%.6f", latLng.lat), this)
        setCoordinate(LOCATION_LNG_KEY, String.format("%.6f", latLng.lng), this)
    }
}

fun isValidLocationSaved(context: Context): Boolean {
    return getLastSavedLocation(context).isValid()
}

fun getUnitType(context: Context): String {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    return if (preferences.contains(UNIT_TYPE_KEY)) {
        preferences.getString(UNIT_TYPE_KEY, DEFAULT_UNIT_TYPE)!!
    } else {
        setUnitType(context, DEFAULT_UNIT_TYPE)
        DEFAULT_UNIT_TYPE
    }
}

fun setUnitType(context: Context, unitType: String) {
    PreferenceManager.getDefaultSharedPreferences(context).apply {
        edit().apply {
            putString(UNIT_TYPE_KEY, unitType)
            apply()
        }
    }
}

fun getCityName(context: Context): String {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    return if (preferences.contains(CITY_NAME_KEY)) {
        preferences.getString(CITY_NAME_KEY, CITY_NOT_SET)!!
    } else {
        setCityName(context, CITY_NOT_SET)
        CITY_NOT_SET
    }
}


fun isCityNameSet(context: Context): Boolean {
    return getCityName(context) != CITY_NOT_SET
}


fun setCityName(context: Context, cityName: String) {
    val name = if (cityName.isBlank()) CITY_NOT_SET else cityName
    PreferenceManager.getDefaultSharedPreferences(context).apply {
        edit().apply {
            putString(CITY_NAME_KEY, name)
            apply()
        }
    }
}

fun getSavedUILanguage(context: Context): String {
    PreferenceManager.getDefaultSharedPreferences(context).apply {
        return if (contains(APP_LANGUAGE_KEY)) {
            getString(APP_LANGUAGE_KEY, DEFAULT_LANGUAGE)!!
        } else {
            setUILanguage(context, DEFAULT_LANGUAGE)
            DEFAULT_LANGUAGE
        }
    }
}

fun setUILanguage(context: Context, newLanguage: String) {
    val language = if (newLanguage.isBlank()) DEFAULT_LANGUAGE else newLanguage

    PreferenceManager.getDefaultSharedPreferences(context).apply {
        edit().apply {
            putString(APP_LANGUAGE_KEY, language)
            apply()
        }
    }
}

fun isTranslationProvidedByApi(context: Context): Boolean {
    return getSavedUILanguage(context) != ALBANIAN
}

fun updateSavedLanguage(context: Context) {
    val deviceLang = Locale.getDefault().language
    if (SupportedLanguages.contains(deviceLang)) {

        val saved = getSavedUILanguage(context)

        if (saved != deviceLang) {
            setUILanguage(context, deviceLang)
        }
    }

}