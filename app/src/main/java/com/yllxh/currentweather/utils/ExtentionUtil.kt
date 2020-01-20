package com.yllxh.currentweather.utils

import android.location.Location
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.yllxh.currentweather.data.json_models.LatLng
import com.yllxh.currentweather.data.reports.Report
import com.yllxh.currentweather.dialogs.DetailsDialog
import com.yllxh.currentweather.dialogs.NotConnectedDialog
import com.yllxh.currentweather.fragments.MainFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalStateException

/**
 * Extension function for the [LifecycleOwner] class, to allow them to
 * observe [LiveData] with a more readable syntax.
 *
 * @param liveData          The LiveData which is to be observed.
 * @param lifecycleOwner    LifecycleOwner which is passed to the LiveData as the LifecycleOwner.
 * @param block             The block of code to be run each time the LiveData is updated.
 */
fun <T> LifecycleOwner.observe(
    liveData: LiveData<T>,
    lifecycleOwner: LifecycleOwner = this,
    block: (T) -> Unit
) {
    liveData.observe(lifecycleOwner, Observer(block))
}

/**
 * Function used to reassign the same value to this [MutableLiveData].
 */
fun <T> MutableLiveData<T>.toSelf() {
    value = value
}

/**
 * Saves new value to this [MutableLiveData].
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
 * Saves new value to this [MutableLiveData].
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

/**
 * Extension function that allows better readability of [Boolean] values of
 * this [LiveData] objects
 *
 * @return Returns true if the stored value is true, false otherwise.
 */
fun LiveData<Boolean>.isTrue(): Boolean {
    return value == true
}

/**
 * Extension function that allows better readability of boolean values of
 * this [LiveData] objects.
 *
 * @return Returns false if the stored value is false, true otherwise.
 */
fun LiveData<Boolean>.isFalse(): Boolean {
    return value == false
}

/**
 * Extension function allows the comparisons of the stored value
 * to the value with is passed as a parameter.
 *
 * @param value The value to be compared to the stored value.
 *
 * @return Returns true if the values are the same, false otherwise.
 */
fun <T> LiveData<T>.holds(value: T): Boolean {
    return this.value == value
}

/**
 * Extension function that launches a coroutine with
 * the Dispatcher.Main as context.
 *
 * @param block The block of code which is to be executed.
 */
fun ViewModel.onMainContext(block : suspend () -> Unit) {
    viewModelScope.launch {
        withContext(Dispatchers.Main){
            block()
        }
    }
}

/**
 * Extension function that launches a coroutine with
 * the Dispatcher.IO as context.
 *
 * @param block The block of code which is to be executed.
 */
fun ViewModel.onIOContext(block : suspend () -> Unit) {
    viewModelScope.launch {
        withContext(Dispatchers.IO){
            block()
        }
    }
}

fun createAlertDialog(binding: ViewDataBinding): AlertDialog {
    return AlertDialog.Builder(binding.root.context)
        .setView(binding.root)
        .create()
}

/**
 * Function that show a [Toast] message with a simpler syntax.
 */
fun Fragment.toast(ms: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), ms, length).show()
}

/**
 * Extension function that converts this instance of a Location
 * to a [LatLng] instance.
 *
 * @return The LatLng instance created by this Location.
 */
fun Location.toLatLng(): LatLng {
    return LatLng(latitude, longitude)
}

/**
 * Log function to show a debug level log.
 *
 * @param ms    The message that is to be logged.
 */
fun Any.log(ms: String){
    Log.d(this.javaClass.simpleName, " || $ms")
}

/**
 * Extension function used to show the [DetailsDialog] on the current [Fragment].
 *
 * @param report    The weather report the dialog is meant for.
 */
fun <T> Fragment.showDetailsDialog(report: T?) where T: Report, T: Parcelable {
    report?.let {
        DetailsDialog.newInstance(report)
            .show(requireFragmentManager(), DetailsDialog.TAG)
    }
}

/**
 * Extension function used to show a [NotConnectedDialog] on the current [Fragment].
 */
fun Fragment.onNotConnected() {
    NotConnectedDialog.newInstance(this)
        .show(requireFragmentManager(), NotConnectedDialog.TAG)
}

