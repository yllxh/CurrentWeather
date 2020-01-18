package com.yllxh.currentweather.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build

class NetworkAlerter (
    private val context: Context,
    private val onStateChanged: (Boolean) -> Unit
) {

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private var connectivityManager: ConnectivityManager = getConnectivityManager()

    init {
        onStateChanged(checkNetworkState())
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
                onStateChanged(hasValidCapabilities(network))
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                onStateChanged(hasValidCapabilities(network))
            }
        }
    }

    private fun getConnectivityManager() =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private fun checkNetworkState(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            return hasValidCapabilities(network)
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    private fun hasValidCapabilities(network: Network): Boolean {
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> true
        }
    }
}