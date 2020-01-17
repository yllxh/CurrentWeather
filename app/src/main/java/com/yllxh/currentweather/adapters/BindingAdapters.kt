package com.yllxh.currentweather.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yllxh.currentweather.R
import com.yllxh.currentweather.data.reports.Report
import com.yllxh.currentweather.data.reports.WeekReport
import com.yllxh.currentweather.utils.*


@BindingAdapter("weekReportData")
fun RecyclerView.setWeekReportData(weekReport: WeekReport?) {
    weekReport?.let {
        when (adapter) {
            is NextHoursReportAdapter -> {
                val hourReportsInADay = context.resources.getInteger(R.integer.next_24_hours_count)
                (adapter as NextHoursReportAdapter).apply {
                    reports = weekReport.hourReports.take(hourReportsInADay)
                }

            }
            is HourReportAdapter -> {
                (adapter as HourReportAdapter).apply {
                    reports = weekReport.dailyReports[0].hourlyReports
                }
            }
            else -> throw IllegalArgumentException(
                "RecycleView adapter type is"
                        + " not supported by binding adapter."
            )
        }
    }
}


@BindingAdapter("report_hour")
fun TextView.setReportHour(report: Report?) {
    report?.let {
        text = fromSecondsToHourString(report.timeInSeconds)
    }
}

@BindingAdapter("report_day")
fun TextView.setReportDay(report: Report?) {
    report?.let {
        text = translateDay(context, report.day)
    }
}

@BindingAdapter("report_description")
fun TextView.setReportDescription(report: Report?) {
    report?.let {
        text =
            getDescriptionFromWeatherId(context, it.weatherId).capitalize()
    }
}

@BindingAdapter("report_dateOfDay")
fun TextView.setDateFromSeconds(report: Report?) {
    report?.let {
        text = fromSecondsToDateStamp(report.timeInSeconds)
    }
}


@BindingAdapter("report_contentDescription")
fun ImageView.setReportContentDescription(report: Report?) {
    report?.let {
        contentDescription =
            getDescriptionFromWeatherId(context, it.weatherId)

    }
}


@BindingAdapter("report_icon")
fun ImageView.setWeatherIcon(report: Report?) {
    report?.apply {
        val drawable = ContextCompat.getDrawable(
            context,
            getWeatherIconId(context, icon)
        )
        setImageDrawable(drawable)
    }
}