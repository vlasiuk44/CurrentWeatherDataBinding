package com.example.currentweatherdatabinding.service

import com.example.currentweatherdatabinding.dataclasses.WeatherData

data class FetchingWeatherEvent(val weatherData: WeatherData)