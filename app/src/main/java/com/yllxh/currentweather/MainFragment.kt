package com.yllxh.currentweather

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.yllxh.currentweather.utils.NetworkAlerter
import com.yllxh.currentweather.utils.NetworkState


class MainFragment : Fragment(), NetworkAlerter.NetworkStateListener {

    private val viewModel  by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        NetworkAlerter.setListener(this, requireContext())

        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onNetworkStateChanged(state: NetworkState) {
        val ms = when (state) {
            NetworkState.AVAILABLE -> "Connected"
            else -> "Lost Connection"
        }
        toast(ms)
    }

}

fun Fragment.toast(ms: String, length: Int = Toast.LENGTH_SHORT){
    Toast.makeText(requireContext(), ms, length).show()
}
