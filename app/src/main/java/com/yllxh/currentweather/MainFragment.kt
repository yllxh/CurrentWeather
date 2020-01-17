package com.yllxh.currentweather

import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yllxh.currentweather.adapters.NextHoursReportAdapter
import com.yllxh.currentweather.databinding.FragmentMainBinding
import com.yllxh.currentweather.utils.*


class MainFragment : Fragment() {

    private val hasLocationPermission get() = hasLocationPermission(requireContext())
    private lateinit var binding: FragmentMainBinding
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.apply {
            nextHoursRecycleView.adapter = NextHoursReportAdapter {}
        }

        observeLiveData()

        return binding.root
    }

    private fun observeLiveData() {
        with(viewModel) {

            observe(todaysReport) { binding.report = it }

            observe(isConnected) {
                if (it) {
                    getTodaysWeatherReport()
                } else if (!searchState.isSuccessful()){
                    onNotConnected()
                }
            }

            observe(isLocationRequested) { wasRequested ->
                if (!wasRequested)
                    return@observe

                if (hasLocationPermission) {
                    getCurrentLocation()
                } else {
                    requestLocationPermission()
                }
            }

            observe(searchState) { onSearchStateChanged(it) }
        }
    }

    private fun onSearchStateChanged(state: SearchState) {
        when (state) {
            SearchState.FAILED -> viewModel.getTodaysWeatherReport()
            SearchState.MISSING_LOCATION_PERMISSION -> toast(getString(R.string.location_permission_denied))
            SearchState.SEARCHING -> toast(getString(R.string.searching))
            else -> return
        }
    }

    private fun onNotConnected() {
        NotConnectedDialog.newInstance(this)
            .show(requireFragmentManager(), NotConnectedDialog.TAG)
    }

    private fun getCurrentLocation() {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            NotConnectedDialog.NOT_CONNECTED_DIALOG_REQUEST -> viewModel.onNotConnectedDialogDismissed()
        }

    }
}

