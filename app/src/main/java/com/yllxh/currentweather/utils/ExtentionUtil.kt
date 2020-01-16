package com.yllxh.currentweather.utils

import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yllxh.currentweather.data.LatLng
import com.yllxh.currentweather.utils.SearchState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


fun MutableLiveData<SearchState>.searching(){
    value = SearchState.SEARCHING
}

fun MutableLiveData<SearchState>.succeeded(){
    value = SearchState.SUCCEEDED
}

fun MutableLiveData<SearchState>.failed(){
    value = SearchState.FAILED
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
fun ViewModel.coroutineScope( block : suspend () -> Unit) = viewModelScope.launch { block() }



fun Fragment.toast(ms: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), ms, length).show()
}

fun Location.toLatLng(): LatLng {
    return LatLng(latitude, longitude)
}

fun Any.log(ms: String){
    Log.d("AAAAAAA", ms)
}


