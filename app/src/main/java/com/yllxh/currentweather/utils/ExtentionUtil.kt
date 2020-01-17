package com.yllxh.currentweather.utils

import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.yllxh.currentweather.data.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException

fun <T> LifecycleOwner.observe(
    liveData: LiveData<T>,
    lifecycleOwner: LifecycleOwner = this,
    block: (T) -> Unit
){
    liveData.observe(lifecycleOwner, Observer(block))
}


fun <T> MutableLiveData<T>.toSelf() {
    value = value
}


/**
 * Extension function for MutableLiveData types that need
 * to save a new value with out worrying about on what thread it is assigned.
 *
 * @param newValue  A new value for this MutableLiveData.
 */
fun <T> MutableLiveData<T>.to(newValue: T) {
    try {
        value = newValue
    } catch (e: IllegalStateException) {
        postValue(newValue)
    }
}


/**
 * Saves new value to this MutableLiveData.
 * New values that are the same as the old one are skip.
 *
 * @param newValue  A new value for this MutableLiveData.
 */
fun <T> MutableLiveData<T>.toNew(newValue: T) {
    if (value == newValue)
        return

    to(newValue)
}

fun LiveData<SearchState>.isMissingPermission(): Boolean {
    return value == SearchState.MISSING_LOCATION_PERMISSION
}

fun LiveData<Boolean>.isTrue(): Boolean {
    return value == true
}

fun LiveData<Boolean>.isFalse(): Boolean {
    return value == false
}


fun LiveData<SearchState>.isSearching(): Boolean {
    return value == SearchState.SEARCHING
}

fun LiveData<SearchState>.isSuccessful(): Boolean {
    return value == SearchState.SUCCEEDED
}

fun LiveData<SearchState>.isFailed(): Boolean {
    return value == SearchState.FAILED
}

fun ViewModel.onMainContext(block : suspend () -> Unit) {
    viewModelScope.launch {
        withContext(Dispatchers.Main){
            block()
        }
    }
}

fun ViewModel.onIOContext(block : suspend () -> Unit) {
    viewModelScope.launch {
        withContext(Dispatchers.IO){
            block()
        }
    }
}


fun Fragment.toast(ms: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), ms, length).show()
}

fun Location.toLatLng(): LatLng {
    return LatLng(latitude, longitude)
}

fun Any.log(ms: String){
    Log.d("AAAAAAA", ms)
}


