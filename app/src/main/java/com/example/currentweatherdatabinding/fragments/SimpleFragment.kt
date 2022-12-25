package com.example.currentweatherdatabinding.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.currentweatherdatabinding.R
import com.example.currentweatherdatabinding.databinding.FragmentSimpleBindingImpl


class SimpleFragment : Fragment() {
    lateinit var dBind: FragmentSimpleBindingImpl
    private val weatherViewModel: WeatherViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dBind = DataBindingUtil.inflate(inflater, R.layout.fragment_simple, container, false)

        weatherViewModel.weatherData.observe(activity as LifecycleOwner) {
            try {
                requireActivity().findViewById<ImageView>(R.id.simple_wind_dir)?.setImageResource(it.wind.pic_dir)
            } catch (e: IllegalStateException) {
                Log.d("FRAGMENT ERR", e.message.toString())
            }
            dBind.weatherData = it
        }

        return dBind.root
    }

    override fun onResume() {
        weatherViewModel.weatherData.value?.wind?.let {
            try {
                requireActivity().findViewById<ImageView>(R.id.simple_wind_dir)?.setImageResource(it.pic_dir)
            } catch (e: IllegalStateException) {
                Log.d("FRAGMENT ERR", e.message.toString())
            }
        }
        super.onResume()
    }
}