package com.yllxh.currentweather.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build

enum class NetworkState { AVAILABLE, LOST }

class NetworkAlerter (
    private val listener: NetworkStateListener,
    private val context: Context
) {
    interface NetworkStateListener {
        fun onNetworkStateChanged(state: NetworkState)
    }

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private var connectivityManager: ConnectivityManager = getConnectivityManager()

    init {
        listener.onNetworkStateChanged(checkNetworkState())
        initNetworkCallback()
        registerNetworkCallback()
    }

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

    private fun checkNetworkState(): NetworkState {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return NetworkState.LOST
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return NetworkState.LOST
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkState.AVAILABLE
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkState.AVAILABLE
                else -> NetworkState.LOST
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return NetworkState.LOST
            return if (nwInfo.isConnected) NetworkState.AVAILABLE else NetworkState.LOST
        }
    }
}