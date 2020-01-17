package com.yllxh.currentweather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yllxh.currentweather.data.reports.DayReport
import com.yllxh.currentweather.databinding.DailyReportItemBinding

class DailyReportAdapter(
    reports: List<DayReport> = listOf(),
    private val ontItemClickListener: (DayReport) -> Unit
    ) : RecyclerView.Adapter<DailyReportAdapter.ViewHolder>() {
    var dailyReports: List<DayReport> = reports
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return dailyReports.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(dailyReports[position], ontItemClickListener)
    }

    class ViewHolder private constructor(private val binding: DailyReportItemBinding)
        :RecyclerView.ViewHolder(binding.root){
        fun bind(dayReport: DayReport, onItemClickListener: (DayReport) -> Unit) {
            binding.report = dayReport
            binding.executePendingBindings()
            binding.root.setOnClickListener { onItemClickListener(dayReport) }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = DailyReportItemBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }
}
