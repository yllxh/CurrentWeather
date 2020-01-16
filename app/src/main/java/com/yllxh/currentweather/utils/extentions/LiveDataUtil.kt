package com.yllxh.currentweather.utils.extentions

import androidx.lifecycle.MutableLiveData
import com.yllxh.currentweather.utils.SearchState

fun MutableLiveData<SearchState>.searching(){
    value = SearchState.SEARCHING
}

fun MutableLiveData<SearchState>.succeeded(){
    value = SearchState.SUCCEEDED
}
fun MutableLiveData<SearchState>.failed(){
    value = SearchState.FAILED
}