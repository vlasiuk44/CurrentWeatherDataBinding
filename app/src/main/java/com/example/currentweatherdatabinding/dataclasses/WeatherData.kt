package com.example.currentweatherdatabinding.dataclasses
import com.squareup.picasso.RequestCreator
import java.io.Serializable

class WeatherData(val description: String, val main: MainWeather, val wind: Wind, val time: String, val icon: RequestCreator?) : Serializable

