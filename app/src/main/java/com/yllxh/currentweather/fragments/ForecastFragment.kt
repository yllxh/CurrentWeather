package com.yllxh.currentweather.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yllxh.currentweather.adapters.DailyReportAdapter
import com.yllxh.currentweather.adapters.HourReportAdapter
import com.yllxh.currentweather.databinding.FragmentForecastBinding
import com.yllxh.currentweather.utils.showDetailsDialog
import com.yllxh.currentweather.utils.translateDay

class ForecastFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentForecastBinding.inflate(inflater, container, false)

        val weekReport = ForecastFragmentArgs.fromBundle(arguments!!).weekReport

        val hourReportAdapter = HourReportAdapter { showDetailsDialog(it)}

        val dailyReportAdapter = DailyReportAdapter {
            hourReportAdapter.reports = it.hourlyReports
            binding.selectedDay = translateDay(requireContext(), it.day)
        }

        binding.apply {
            dailyReportRecycleView.adapter = dailyReportAdapter
            hourlyReportRecycleView.adapter = hourReportAdapter
            selectedDay = translateDay(requireContext(), weekReport.dailyReports[0].day)
            this.weekReport = weekReport
        }

        return binding.root
    }
}














