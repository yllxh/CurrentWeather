package com.yllxh.currentweather.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.yllxh.currentweather.data.reports.Report
import com.yllxh.currentweather.utils.fromSecondsToDateStamp
import com.yllxh.currentweather.utils.getDescriptionFromWeatherId
import com.yllxh.currentweather.utils.getWeatherIconId
import com.yllxh.currentweather.utils.translateDay


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