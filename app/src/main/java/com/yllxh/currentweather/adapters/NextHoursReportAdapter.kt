package com.yllxh.currentweather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yllxh.currentweather.data.reports.HourReport
import com.yllxh.currentweather.databinding.CompatHourReportItemBinding

class NextHoursReportAdapter(
    reports: List<HourReport> = listOf(),
    private val ontItemClickListener: (HourReport) -> Unit
): RecyclerView.Adapter<NextHoursReportAdapter.ViewHolder>() {
    var reports: List<HourReport> = reports
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return reports.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(reports[position], ontItemClickListener)
    }

    class ViewHolder private constructor(private val binding: CompatHourReportItemBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(hourReport: HourReport, onItemClickListener: (HourReport) -> Unit) {
            binding.report = hourReport
            binding.executePendingBindings()
            binding.root.setOnClickListener { onItemClickListener(hourReport) }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = CompatHourReportItemBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }
}