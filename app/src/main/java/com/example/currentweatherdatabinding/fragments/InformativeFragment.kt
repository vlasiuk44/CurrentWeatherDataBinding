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
import com.example.currentweatherdatabinding.databinding.FragmentInformativeBindingImpl
import java.lang.Exception


class InformativeFragment : Fragment() {
    lateinit var dBind: FragmentInformativeBindingImpl
    private val weatherViewModel: WeatherViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dBind = DataBindingUtil.inflate(inflater, R.layout.fragment_informative, container, false)

        weatherViewModel.weatherData.observe(activity as LifecycleOwner) {
            try {
                requireActivity().findViewById<ImageView>(R.id.informative_wind_dir)?.setImageResource(it.wind.pic_dir)
                requireActivity().findViewById<ImageView>(R.id.informative_wind_speed)?.setImageResource(it.wind.pic_speed)
                it.icon?.into(requireActivity().findViewById<ImageView>(R.id.weather_icon))
            } catch (e: Exception) {
                Log.d("FRAGMENT ERR", e.message.toString())
            }

            dBind.weatherData = it
        }
        return dBind.root
    }

    override fun onResume() {
        weatherViewModel.weatherData.value?.let {
            try {
                requireActivity().findViewById<ImageView>(R.id.informative_wind_dir)?.setImageResource(it.wind.pic_dir)
                requireActivity().findViewById<ImageView>(R.id.informative_wind_speed)?.setImageResource(it.wind.pic_speed)
                it.icon?.into(requireActivity().findViewById<ImageView>(R.id.weather_icon))
            } catch (e: Exception) {
                Log.d("FRAGMENT ERR", e.message.toString())
            }
        }
        super.onResume()
    }
}