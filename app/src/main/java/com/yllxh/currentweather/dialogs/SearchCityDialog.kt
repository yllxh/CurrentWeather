package com.yllxh.currentweather.dialogs


import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.yllxh.currentweather.R
import com.yllxh.currentweather.databinding.DialogEnterCityNameBinding
import com.yllxh.currentweather.utils.SearchState
import com.yllxh.currentweather.utils.createAlertDialog
import com.yllxh.currentweather.utils.toast


class SearchCityDialog : DialogFragment() {

    private lateinit var binding: DialogEnterCityNameBinding
    private var cityName: String = ""
    private var searchState: SearchState? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_enter_city_name,
            null,
            false
        )
        val dialog = createAlertDialog(binding)

        searchState = arguments?.getParcelable(CITY_NAME_KEY)

        searchState?.let {
            if (searchState == SearchState.FAILED) toast(getString(R.string.search_failed))
        }

        binding.apply {

            cancelButton.setOnClickListener {
                dialog.cancel()
            }

            searchButton.setOnClickListener {
                cityName = editText.text.toString()
                dialog.cancel()
            }
        }

        return dialog
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        if (cityName.isBlank())
            return

        val intent = Intent().apply {
            putExtra(SEARCHED_CITY_NAME_KEY, cityName)
        }
        targetFragment?.onActivityResult(SEARCH_CITY_DIALOG_REQUEST, Activity.RESULT_OK, intent)
    }

    companion object {
        const val SEARCH_CITY_DIALOG_REQUEST = 101
        const val SEARCHED_CITY_NAME_KEY = "searchedCityNameKey"
        private const val CITY_NAME_KEY = "EnterCityNameDialogKey"
        const val TAG = "EnterCityNameDialogTag"
        fun newInstance(
            fragment: Fragment,
            searchState: SearchState = SearchState.UNDEFINED
        ): SearchCityDialog {
            return SearchCityDialog().apply {
                setTargetFragment(fragment, SEARCH_CITY_DIALOG_REQUEST)
                arguments = Bundle().apply { putParcelable(CITY_NAME_KEY, searchState) }
            }
        }
    }
}
