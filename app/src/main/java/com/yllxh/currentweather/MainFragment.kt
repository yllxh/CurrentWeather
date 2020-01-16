package com.yllxh.currentweather

import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.yllxh.currentweather.databinding.FragmentMainBinding
import com.yllxh.currentweather.utils.*


class MainFragment : Fragment() {

    private val hasLocationPermission get() = hasLocationPermission(requireContext())
    private lateinit var binding : FragmentMainBinding
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainBinding.inflate(inflater, container, false)

        NetworkAlerter.setListener(viewModel, requireContext(), lifecycle)

        viewModel.isConnected.observe(this, Observer {
            it?.let { onNetworkStateChanged(it) }
        })

        viewModel.todaysReport.observe(this, Observer {
            it?.let {
                binding.report = it
            }
        })

        viewModel.isLocationRequested.observe(this, Observer { wasRequested ->
            wasRequested?.let {
                if (!wasRequested)
                    return@let

                if (hasLocationPermission){
                    requestCurrentLocation()
                } else {
                    requestLocationPermission()
                }

            }
        })

        return binding.root
    }

    private fun requestCurrentLocation() {
        LocationRetriever(requireContext()) {
            viewModel.onLocationRetrieved(it)
        }
    }

    private fun requestLocationPermission() =
        requestLocationPermission(this, LOCATION_PERMISSION_REQUEST)

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.onLocationPermissionApproved()
                } else {
                    viewModel.onLocationPermissionDenied()
                }
            }
        }
    }

    private fun onNetworkStateChanged(isConnected: Boolean) {
        val ms = when {
            isConnected -> "Connected"
            else -> "Internet connection lost."
        }
    }

}

