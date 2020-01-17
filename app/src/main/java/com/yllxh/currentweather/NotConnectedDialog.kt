package com.yllxh.currentweather

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

class NotConnectedDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.internet_required))
            .setMessage(R.string.internet_required_message)
            .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.cancel() }
            .create()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)

        targetFragment?.onActivityResult(NOT_CONNECTED_DIALOG_REQUEST, Activity.RESULT_OK, Intent())
    }

    companion object {
        const val NOT_CONNECTED_DIALOG_REQUEST = 111
        const val TAG = "not_connected_dialog_tag"

        fun newInstance(fragment: Fragment): NotConnectedDialog {
            return NotConnectedDialog().apply {
                setTargetFragment(fragment, NOT_CONNECTED_DIALOG_REQUEST)
            }
        }
    }
}
