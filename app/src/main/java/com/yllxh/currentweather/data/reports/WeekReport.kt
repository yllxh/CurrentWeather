package com.yllxh.currentweather.data.reports



import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.yllxh.currentweather.data.json_models.City
import com.yllxh.currentweather.utils.fromHourlyToDayReports
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeekReport(
    @SerializedName("city") val city: City = City(),
    @SerializedName("list") val hourReports: List<HourReport> = listOf()
) : HourlyReport(hourReports), Parcelable {
    val dailyReports get() = fromHourlyToDayReports(hourReports)
}
