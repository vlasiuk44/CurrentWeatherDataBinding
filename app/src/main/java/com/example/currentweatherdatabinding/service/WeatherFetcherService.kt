package com.example.currentweatherdatabinding.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.currentweatherdatabinding.utils.WeatherUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class WeatherFetcherService : Service() {

    private var activeThread: Thread? = null
    var cityName = ""

    override fun onStart(intent: Intent?, startId: Int) {
        createThread(intent)
    }

    private fun createThread(intent: Intent?) {
        if (intent == null) {
            return
        }
        cityName = intent.getStringExtra("cityName").toString()
        val API_KEY = intent.getStringExtra("API_KEY")

        if (cityName != null && cityName != "" && API_KEY != null && API_KEY != "") {
            activeThread = Thread {
                while (true) {
                    if (cityName != "") {
                        val weatherData = WeatherUtils.loadWeather(cityName, API_KEY)
                        if (weatherData != null) {
                            EventBus.getDefault().post(FetchingWeatherEvent(weatherData))
                        }
                    }
                    Thread.sleep(300_000)
                }
            }
            activeThread!!.start()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createThread(intent)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}