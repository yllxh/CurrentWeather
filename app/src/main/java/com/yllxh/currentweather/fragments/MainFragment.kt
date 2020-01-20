package com.yllxh.currentweather.fragments

import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.yllxh.currentweather.viewmodels.MainViewModel
import com.yllxh.currentweather.dialogs.NotConnectedDialog
import com.yllxh.currentweather.R
import com.yllxh.currentweather.adapters.NextHoursReportAdapter
import com.yllxh.currentweather.databinding.FragmentMainBinding
import com.yllxh.currentweather.dialogs.SearchCityDialog
import com.yllxh.currentweather.utils.*
import com.yllxh.currentweather.fragments.MainFragmentDirections.actionMainFragmentToForecastFragment as toForecastFragment


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
        binding.nextHoursRecycleView.adapter = NextHoursReportAdapter { showDetailsDialog(it) }

        observeLiveData()
        setOnClickListeners()
        return binding.root
    }


    private fun setOnClickListeners() {
        with(binding) {

            dailyForecastButton.setOnClickListener { navigateToForecastFragment()}

            detailsButton.setOnClickListener { showDetailsDialog(viewModel?.todaysReport?.value)}

            searchButton.setOnClickListener { showSearchDialog() }
        }
    }

    private fun observeLiveData() {
        with(viewModel) {

            observe(todaysReport) { binding.report = it }

            observe(isConnected) {
                if (searchState.holds(SearchState.SUCCEEDED))
                    return@observe

                when {
                    it -> getTodaysWeatherReport()
                    !searchState.holds(SearchState.SUCCEEDED) -> onNotConnected()
                }
            }

            observe(isLocationRequested) { wasRequested ->
                if (!wasRequested)
                    return@observe

                if (hasLocationPermission) {
                    LocationRetriever(requireContext()) {
                        viewModel.onLocationRetrieved(it)
                    }
                } else {
                    requestLocationPermission(this@MainFragment)
                }
            }

            observe(searchState) { onSearchStateChanged(it) }
        }
    }

    private fun onSearchStateChanged(state: SearchState) {
        when (state) {
            SearchState.FAILED -> viewModel.getTodaysWeatherReport()
            SearchState.MISSING_LOCATION_PERMISSION -> toast(getString(R.string.location_permission_denied))
            else -> return
        }
    }

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
            SearchCityDialog.SEARCH_CITY_DIALOG_REQUEST -> {
                val cityName = data?.getStringExtra(SearchCityDialog.SEARCHED_CITY_NAME_KEY) ?: ""
                viewModel.onSearchCityDismissed(cityName)
            }
        }
    }

    private fun navigateToForecastFragment() {
        viewModel.weekReport.value?.let {
            findNavController().navigate(toForecastFragment(it))
        }
    }
    private fun showSearchDialog() {
        SearchCityDialog.newInstance(this)
            .show(requireFragmentManager(), SearchCityDialog.TAG)
    }
}

