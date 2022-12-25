package com.example.currentweatherdatabinding.utils

import android.annotation.SuppressLint
import android.util.Log
import com.example.currentweatherdatabinding.R
import com.example.currentweatherdatabinding.dataclasses.MainWeather
import com.example.currentweatherdatabinding.dataclasses.WeatherData
import com.example.currentweatherdatabinding.dataclasses.Wind
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class WeatherUtils {
    companion object {

        @SuppressLint("SimpleDateFormat")
        val formatter = SimpleDateFormat("dd MM yyyy HH:mm")
        private val gson = Gson()
        private val picasso = Picasso.get()

        fun loadWeather(cityName: String, API_KEY: String): WeatherData? {
            val weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=${cityName}&appid=${API_KEY}&units=metric&lang=ru";
            try {
                val stream = URL(weatherURL).getContent() as InputStream
                val data = JSONTokener(Scanner(stream).nextLine()).nextValue() as JSONObject

                if (data.get("cod").toString().toInt() >= 400) {
                    // smth went wrong
                }

                val main = gson.fromJson(data.get("main").toString(), MainWeather::class.java)
                val speed = (data.get("wind") as JSONObject).get("speed").toString().toDouble()
                val speed_pic = defineWindSpeed(speed)
                val (direction, picture) = defineWindDirection((data.get("wind") as JSONObject).get("deg").toString().toInt())
                val wind = Wind(speed, direction, picture, speed_pic)
                val description = ((data.get("weather") as JSONArray)[0] as JSONObject).get("description").toString()
                val icon_code = ((data.get("weather") as JSONArray)[0] as JSONObject).get("icon").toString()
                val icon = loadPicture(icon_code)
                val date = formatter.format(Date(Timestamp(data.get("dt").toString().toLong()).time * 1000))

                return WeatherData(description, main, wind, date, icon)
            } catch (e: IOException) {
                return null
            }
            return null
        }

        private fun loadPicture(icon_code: String): RequestCreator? {
            val iconURL = "http://openweathermap.org/img/wn/${icon_code}@2x.png"
            return try {
                picasso.load(iconURL)
            } catch (e: IOException) {
                Log.d("PIC_LOADING", "error while loading picture")
                null
            }
        }

        private fun defineWindSpeed(speed: Double): Int {
            if (speed < 0.2) {
                return R.drawable.ic_wind_small
            } else if (speed < 5) {
                return R.drawable.ic_wind_mild
            } else if (speed < 10) {
                return R.drawable.ic_wind_medium
            } else if (speed < 17) {
                return R.drawable.ic_wind_strong
            }
            return R.drawable.ic_wind_huge
        }

        private fun defineWindDirection(azimut: Int): Pair<String, Int> {
            val azimuts = intArrayOf(0, 45, 90, 135, 180, 225, 270, 315, 359)
            for (i in azimuts.indices) {
                azimuts[i] = abs(azimuts[i] - azimut)
            }
            var minDir = 360
            var minIndex = 0
            for (i in azimuts.indices) {
                if (azimuts[i] < minDir) {
                    minDir = azimuts[i]
                    minIndex = i
                }
            }
            var direction: String = ""
            var picture: Int = -1
            when (minIndex) {
                0, 8 -> {
                    direction = "c"
                    picture = R.drawable.ic_up
                }
                1 -> {
                    direction = "c/в"
                    picture = R.drawable.ic_up
                }
                2 -> {
                    direction = "в"
                    picture = R.drawable.ic_right
                }
                3 -> {
                    direction = "ю/в"
                    picture = R.drawable.ic_right
                }
                4 -> {
                    direction = "ю"
                    picture = R.drawable.ic_down
                }
                5 -> {
                    direction = "ю/з"
                    picture = R.drawable.ic_down
                }
                6 -> {
                    direction = "з"
                    picture = R.drawable.ic_right
                }
                7 -> {
                    direction = "с/з"
                    picture = R.drawable.ic_right
                }
                else -> {
                    direction = "c"
                    picture = R.drawable.ic_up
                }
            }
            return Pair(direction, picture)
        }
    }
}