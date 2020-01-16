package com.yllxh.currentweather

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.yllxh.currentweather.databinding.FragmentMainBinding
import com.yllxh.currentweather.utils.NetworkAlerter
import com.yllxh.currentweather.utils.NetworkState
import com.yllxh.currentweather.utils.toast


class MainFragment : Fragment() {

    private lateinit var binding : FragmentMainBinding
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainBinding.inflate(inflater, container, false)

        NetworkAlerter.setListener(viewModel, requireContext(), lifecycle)

        viewModel.isConnected.observe(this, Observer {
            it?.let { onNetworkStateChanged(it) }
        })

        viewModel.todaysReport.observe(this, Observer {
            it?.let {
                binding.report = it
                toast("dataFetched")
            }
        })

        return binding.root
    }

    private fun onNetworkStateChanged(isConnected: Boolean) {
        val ms = when {
            isConnected -> "Connected"
            else -> "Internet connection lost."
        }
    }

}

