package com.yllxh.currentweather.utils

import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.yllxh.currentweather.data.LatLng
import com.yllxh.currentweather.data.reports.DayReport
import com.yllxh.currentweather.data.reports.Report
import com.yllxh.currentweather.data.reports.WeekReport
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
 * Saves new value to this MutableLiveData.
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
 * Old values are skip.
 *
 * @param newValue  A new value for this MutableLiveData.
 *
 * @return  Returns true if the value is saved, false otherwise.
 */
fun <T> MutableLiveData<T>.toNew(newValue: T): Boolean {
    if (value == newValue)
        return false

    to(newValue)
    return true
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


fun DialogFragment.createAlertDialog(binding: ViewDataBinding): AlertDialog {
    return AlertDialog.Builder(binding.root.context)
        .setView(binding.root)
        .create()
}


fun Fragment.toast(ms: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), ms, length).show()
}

fun Location.toLatLng(): LatLng {
    return LatLng(latitude, longitude)
}

fun Any.log(ms: String){
    Log.d(this.javaClass.simpleName, " || $ms")
}


