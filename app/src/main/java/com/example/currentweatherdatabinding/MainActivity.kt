package com.example.currentweatherdatabinding

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.currentweatherdatabinding.alertDialog.DialogViewModel
import com.example.currentweatherdatabinding.alertDialog.DisplayingMode
import com.example.currentweatherdatabinding.alertDialog.SettingsDialog
import com.example.currentweatherdatabinding.citiesRecycler.CitiesAdapter
import com.example.currentweatherdatabinding.citiesRecycler.ScrollPagerListener
import com.example.currentweatherdatabinding.databinding.ActivityMainBinding
import com.example.currentweatherdatabinding.fragments.InformativeFragment
import com.example.currentweatherdatabinding.fragments.SimpleFragment
import com.example.currentweatherdatabinding.fragments.WeatherViewModel
import com.example.currentweatherdatabinding.service.FetchingWeatherEvent
import com.example.currentweatherdatabinding.service.WeatherFetcherService
import com.example.currentweatherdatabinding.utils.WeatherUtils
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {

    private lateinit var dBind: ActivityMainBinding
    @SuppressLint("SimpleDateFormat")
    lateinit var API_KEY: String
    private var cityName = ""
    private lateinit var weatherFetcherService: WeatherFetcherService
    private lateinit var settingsBtn: ImageView
    lateinit var settingsDialog: SettingsDialog
    private val dialogViewModel: DialogViewModel by viewModels()
    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var citiesRecyclerView: RecyclerView
    lateinit var fragmentManager: FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction
    lateinit var informativeFragment: Fragment
    lateinit var simpleFragment: Fragment
    lateinit var citiesNames: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dBind = DataBindingUtil.setContentView(this, R.layout.activity_main)

        settingsBtn = findViewById(R.id.setting_btn)
        citiesRecyclerView = findViewById(R.id.cities_recycler)
        citiesNames = resources.getStringArray(R.array.cities)
        API_KEY = getString(R.string.API_KEY)
        cityName = citiesNames[0]

        weatherFetcherService = WeatherFetcherService()
        dialogViewModel.displayingMode.value = DisplayingMode.INFORMATIVE
        settingsDialog = SettingsDialog(this)
        initRecyclerView()

        fragmentManager = supportFragmentManager
        initFragments()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.lang_menu)
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setOnMenuItemClickListener { item ->
            handleChangeLocalization(item)
            true
        }

        EventBus.getDefault().register(this)
        startFetchingWeather()

        dialogViewModel.displayingMode.observe( this) {
            fragmentTransaction = fragmentManager.beginTransaction()

            if (it == DisplayingMode.INFORMATIVE && !informativeFragment.isVisible) {
                fragmentTransaction.replace(R.id.fragment_container_view, informativeFragment).commit()
            } else if (it == DisplayingMode.SIMPLE && !simpleFragment.isVisible) {
                fragmentTransaction.replace(R.id.fragment_container_view, simpleFragment).commit()
            }
        }
    }

    fun handleChangeLocalization(item: MenuItem) {
        Log.d("Menu item", item.toString())

    }

    fun onSettingsButtonClick(v: View) {
        settingsDialog.show(supportFragmentManager, "SettingsModal")
    }

    override fun onResume() {
        super.onResume()
        startFetchingWeather()
    }

    override fun onRestart() {
        super.onRestart()
        startFetchingWeather()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    private fun initFragments() {
        fragmentTransaction = fragmentManager.beginTransaction()
        informativeFragment = InformativeFragment()
        simpleFragment = SimpleFragment()

        fragmentManager.beginTransaction()
            .add(R.id.fragment_container_view, if (dialogViewModel.displayingMode.value == DisplayingMode.INFORMATIVE) informativeFragment else simpleFragment)
            .commit()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun pagerItemChangeListener(newPosition: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            cityName = citiesNames[newPosition]
            val weatherData = WeatherUtils.loadWeather(citiesNames[newPosition], API_KEY)
            if (weatherData != null) {
                dBind.weatherData = weatherData
                weatherViewModel.weatherData.postValue(weatherData)
            }
        }
    }

    private fun initRecyclerView() {
        val citiesAdapter = CitiesAdapter(citiesNames, this)
        citiesRecyclerView.adapter = citiesAdapter

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        citiesRecyclerView.layoutManager = layoutManager

        val pagerHelper = PagerSnapHelper()
        pagerHelper.attachToRecyclerView(citiesRecyclerView)
        val scrollPagerHelper = ScrollPagerListener(pagerHelper, ::pagerItemChangeListener)
        citiesRecyclerView.addOnScrollListener(scrollPagerHelper)
    }

    private fun startFetchingWeather() {
        Intent(this, WeatherFetcherService::class.java).also { intent ->
            intent.putExtra("cityName", cityName)
            intent.putExtra("API_KEY", API_KEY)
            startService(intent)
        }
    }

    @Subscribe
    fun onFetchingWeatherEvent(fetchingWeatherEvent: FetchingWeatherEvent) {
        dBind.weatherData = fetchingWeatherEvent.weatherData
        weatherViewModel.weatherData.postValue(fetchingWeatherEvent.weatherData)
    }
}