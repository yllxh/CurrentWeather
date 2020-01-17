package com.yllxh.currentweather.dialogs


import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.yllxh.currentweather.R
import com.yllxh.currentweather.data.reports.Report
import com.yllxh.currentweather.databinding.DialogDetailsBinding

class DetailsDialog: DialogFragment() {

    private lateinit var binding: DialogDetailsBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_details,
            null,
            false
        )

        val dialog = createAlertDialog(binding)
        (arguments!!.getParcelable<Parcelable>(KEY) as Report).let{
            binding.report = it
            binding.dialog = this
        }
        return dialog
    }


    companion object {
        private const val KEY = "DialogDetailsKey"
        const val TAG = "DialogDetailsTag"
        fun <T>newInstance(report: T): DetailsDialog where T : Parcelable, T: Report{
            return DetailsDialog().apply {
                this.arguments = Bundle().apply {putParcelable(KEY, report)}
            }
        }
    }

    private fun createAlertDialog(binding: ViewDataBinding): AlertDialog {
        return AlertDialog.Builder(binding.root.context)
            .setView(binding.root)
            .create()
    }

}
