package com.yllxh.currentweather.data.reports


class DayReport (val hourlyReports: List<HourReport>)
    : HourlyReport(hourlyReports)