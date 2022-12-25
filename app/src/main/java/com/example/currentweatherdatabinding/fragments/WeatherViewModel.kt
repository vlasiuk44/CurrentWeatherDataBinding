package com.example.currentweatherdatabinding.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.currentweatherdatabinding.dataclasses.WeatherData

class WeatherViewModel: ViewModel() {
    val weatherData: MutableLiveData<WeatherData> by lazy {
        MutableLiveData()
    }
}