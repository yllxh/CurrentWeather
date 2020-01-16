package com.yllxh.currentweather.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

enum class NetworkState { AVAILABLE, LOST }

class NetworkAlerter private constructor(
    private val listener: NetworkStateListener,
    private val context: Context,
    lifecycle: Lifecycle
) : LifecycleObserver {

    interface NetworkStateListener {
        fun onNetworkStateChanged(state: NetworkState)
    }

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private var connectivityManager: ConnectivityManager = getConnectivityManager()

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startLisening() {
        initNetworkCallback()
        registerNetworkCallback()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopListening() = connectivityManager.unregisterNetworkCallback(networkCallback)

    private fun registerNetworkCallback() {
        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            networkCallback
        )
    }

    private fun initNetworkCallback() {
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                listener.onNetworkStateChanged(NetworkState.AVAILABLE)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                listener.onNetworkStateChanged(NetworkState.LOST)
            }
        }
    }

    private fun getConnectivityManager() =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    companion object {
        fun setListener(listener: NetworkStateListener, context: Context, lifecycle: Lifecycle) {
            NetworkAlerter(listener, context, lifecycle)
        }
    }

}